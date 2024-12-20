package com.example.project3_algo;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private List<Bucket<K, V>> buckets;
    private int size;

    public HMap() {
        this.buckets = new ArrayList<>(INITIAL_CAPACITY);
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buckets.add(new Bucket<>());
        }
        this.size = 0;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.size();
    }

    public void put(K key, V value) {
        int index = hash(key);
        Bucket<K, V> bucket = buckets.get(index);
        Entry<K, V> existingEntry = bucket.getEntry(key);

        if (existingEntry != null) {
            existingEntry.setValue(value);
        } else {
            bucket.addEntry(new Entry<>(key, value));
            size++;
            if (size / (float) buckets.size() > LOAD_FACTOR) {
                rehash();
            }
        }
    }

    public V get(K key) {
        int index = hash(key);
        Bucket<K, V> bucket = buckets.get(index);
        Entry<K, V> entry = bucket.getEntry(key);
        return entry != null ? entry.getValue() : null;
    }

    public void remove(K key) {
        int index = hash(key);
        Bucket<K, V> bucket = buckets.get(index);
        bucket.removeEntry(key);
        size--;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private void rehash() {
        List<Bucket<K, V>> oldBuckets = buckets;
        buckets = new ArrayList<>(oldBuckets.size() * 2);
        for (int i = 0; i < oldBuckets.size() * 2; i++) {
            buckets.add(new Bucket<>());
        }
        size = 0;

        for (Bucket<K, V> bucket : oldBuckets) {
            for (Entry<K, V> entry : bucket.getEntries()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static class Bucket<K, V> {
        private List<Entry<K, V>> entries;

        public Bucket() {
            this.entries = new LinkedList<>();
        }

        public void addEntry(Entry<K, V> entry) {
            entries.add(entry);
        }

        public Entry<K, V> getEntry(K key) {
            for (Entry<K, V> entry : entries) {
                if (entry.getKey().equals(key)) {
                    return entry;
                }
            }
            return null;
        }

        public void removeEntry(K key) {
            entries.removeIf(entry -> entry.getKey().equals(key));
        }

        public List<Entry<K, V>> getEntries() {
            return entries;
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}

