package bearmaps.proj2d;

import bearmaps.lab9.MyTrieSet;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, zetong
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private KDTree kdTree;
    Map<Point, Long> map = new HashMap<>();
    MyTrieSet trieSet;
    private Map<String, List<Node>> cleanedNameToNodes;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        List<Point> points = new ArrayList<>();
        trieSet = new MyTrieSet();
        cleanedNameToNodes = new HashMap<>();
        List<Node> nodesList;

        for (Node node : nodes) {
            /* If the node has a name, clean it, then add it to the trieSet,
            and put the (cleaned name, list of nodes) pair into the cleanedNameToNodes map. */
            if (node.name() != null) {
                String nodeName = node.name();
                String cleanedName = cleanString(nodeName);
                trieSet.add(cleanedName);

                if (!cleanedNameToNodes.containsKey(cleanedName)) {
                    LinkedList<Node> list = new LinkedList<>();
                    list.add(node);
                    cleanedNameToNodes.put(cleanedName, list);
                } else {
                    nodesList = cleanedNameToNodes.get(cleanedName);
                    nodesList.add(node);
                    cleanedNameToNodes.put(cleanedName, nodesList);
                }
            }

            // Only consider the nodes that has neighbour
            long id = node.id();
            if (!neighbors(id).isEmpty()) {
                Point point = new Point(node.lon(), node.lat());
                points.add(point);
                map.put(point, id);
            }
        }
        kdTree = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearest = kdTree.nearest(lon, lat);
        return map.get(nearest);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        Set<String> locationsSet = new HashSet<>();
        String cleanedPrefix = cleanString(prefix);
        List<String> matchedCleanNames = trieSet.keysWithPrefix(cleanedPrefix);

        for (String name : matchedCleanNames) {
            List<Node> nodes = cleanedNameToNodes.get(name);
            for (Node node : nodes) {
                locationsSet.add(node.name());
            }
        }

        return new LinkedList<>(locationsSet);
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleanedName = cleanString(locationName);
        List<Map<String, Object>> targetNode = new ArrayList<>();

        if (cleanedNameToNodes.containsKey(cleanedName)) {
            for (Node node : cleanedNameToNodes.get(cleanedName)) {
                Map<String, Object> locationInfo = new HashMap<>();
                locationInfo.put("id", node.id());
                locationInfo.put("name", node.name());
                locationInfo.put("lon", node.lon());
                locationInfo.put("lat", node.lat());
                targetNode.add(locationInfo);
            }
        }

        return targetNode;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
