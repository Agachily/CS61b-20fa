package bearmaps.proj2c;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.*;


public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private AStarGraph<Vertex> input;
    private Vertex start;
    private Vertex end;
    private double timeout;
    private List<Vertex> solution;
    private ExtrinsicMinPQ<Vertex> pQ;
    private Map<Vertex, Double> distTo;
    private Map<Vertex, Vertex> edgeTo;
    private int numStatesExplored; // The total number of priority queue dequeue operations.
    double totalTime;
    SolverOutcome outcome;
    Stopwatch sw;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.input = input;
        this.start = start;
        this.end = end;
        this.timeout = timeout;
        // Priority p = v's distance + heuristic distance
        //this.pQ = new DoubleMapPQ<>();
        this.pQ = new ArrayHeapMinPQ<>();
        solution = new ArrayList<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        distTo.put(start, (double) 0);
        edgeTo.put(start, null);
        sw = new Stopwatch();

        // Insert the source vertex into pQ
        double estimatedDist = input.estimatedDistanceToGoal(start, end);
        pQ.add(start, estimatedDist);

        /* Calculate the shortest path.
           Repeat until the pQ is empty, pQ.getSmallest() is the goal or timeout is exceeded */
        while (pQ.size() > 0) {
            Vertex currentSmallest = pQ.getSmallest();
            numStatesExplored++;
            pQ.removeSmallest();

            if (currentSmallest.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                totalTime = sw.elapsedTime();
                return;
            }

            // Check whether run out of time
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                totalTime = sw.elapsedTime();
                return;
            }

            List<WeightedEdge<Vertex>> neighbors = input.neighbors(currentSmallest);
            for (WeightedEdge<Vertex> vertex : neighbors) {
                relax(vertex);
            }
        }

        // UNSOLVABLE if the priority queue became empty
        outcome = SolverOutcome.UNSOLVABLE;
        totalTime = sw.elapsedTime();
    }

    private void relax(WeightedEdge<Vertex> v) {
        Vertex fromVertex = v.from();
        Vertex toVertex = v.to();
        double weight = v.weight();
        double newDist = distTo.get(fromVertex) + weight;
        double estimatedDistFromToVertexToGoal = input.estimatedDistanceToGoal(toVertex, end);

        if (!distTo.containsKey(toVertex)) {
            distTo.put(toVertex, newDist);
            relaxHelper(fromVertex, toVertex, estimatedDistFromToVertexToGoal);
        }

        if (newDist < distTo.get(toVertex)) {
            distTo.replace(toVertex, newDist);
            relaxHelper(fromVertex, toVertex, estimatedDistFromToVertexToGoal);
        }
    }

    private void relaxHelper(Vertex fromVertex, Vertex toVertex, double estimatedDistFromToVertexToGoal) {
        edgeTo.put(toVertex, fromVertex);
        if (pQ.contains(toVertex)) {
            pQ.changePriority(toVertex, distTo.get(toVertex) + estimatedDistFromToVertexToGoal);
        } else {
            pQ.add(toVertex, distTo.get(toVertex) + estimatedDistFromToVertexToGoal);
        }
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() {
        if (outcome == SolverOutcome.UNSOLVABLE || outcome == SolverOutcome.TIMEOUT) {
            return solution;
        } else {
            solution.clear();
            Collections.reverse(solutionHelper(edgeTo, end));
            return solution;
        }
    }

    // Get a list of vertices corresponding to a solution from the edgeTO map
    private List<Vertex> solutionHelper(Map<Vertex, Vertex> edgeTo, Vertex endVertex) {
        if (endVertex == null) {
            return solution;
        }
        solution.add(endVertex);
        return solutionHelper(edgeTo, edgeTo.get(endVertex));
    }

    public double solutionWeight() {
        if (outcome == SolverOutcome.UNSOLVABLE || outcome == SolverOutcome.TIMEOUT) {
            return 0;
        } else {
            return distTo.get(end);
        }
    }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return totalTime;
    }
}

