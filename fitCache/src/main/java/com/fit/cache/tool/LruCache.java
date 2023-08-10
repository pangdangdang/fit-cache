package com.fit.cache.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songhao
 */
public class LruCache {
    class Node {
        String key;
        Object value;
        Node prev;
        Node next;

        public Node(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
    private final int capacity;
    private final Map<String, Node> cache;
    private Node head;
    private Node tail;

    public LruCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node("head", 0);
        this.tail = new Node("tail", 0);
        head.next = tail;
        tail.prev = head;
    }

    public Object get(String key) {
        Node node = cache.get(key);
        if (node != null) {
            // l列表里面有就转到头部
            moveToHead(node);
            return node.value;
        }
        return null;
    }

    public synchronized void put(String key, Object value) {
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            node = new Node(key, value);
            cache.put(key, node);
            addToHead(node);
            if (cache.size() > capacity) {
                // 超过容量就删除尾部节点
                Node removedNode = removeTail();
                cache.remove(removedNode.key);
            }
        }
    }

    private synchronized void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private synchronized void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private synchronized void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private synchronized Node removeTail() {
        Node removedNode = tail.prev;
        removeNode(removedNode);
        return removedNode;
    }

}
