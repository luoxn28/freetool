```java
/**
 * 生成指定区间N个不同的随机数，区间范围 [origin, bound)
 */
@SuppressWarnings("unchecked")
private static List<Integer> randomUniques(int num, int origin, int bound) {
    if (num <= 0 || origin >= bound) {
        return Collections.EMPTY_LIST;
    }

    Function<Integer, Integer> value = integer -> origin + integer;
    Map<Integer, Integer> cache = new HashMap<>();

    List<Integer> result = new ArrayList<>();
    for (int i = bound - 1; i >= 0 && result.size() < num; i--) {
        int rand = ThreadLocalRandom.current().nextInt(i + 1);
        result.add(value.apply(cache.getOrDefault(rand, rand)));
        cache.put(rand, i);
    }

    return result;
}
```

如果只是想生成区间内N个随机数，可直接使用如下方法：
```java
ThreadLocalRandom.current().ints(10, 0, 100).boxed().collect(Collectors.toList())
```
