package com.unl.estrdts.Practica3;

public class ShellSort {

    public static void shellSort(Linkendlist<Integer> vec) throws Exception {
        final Integer N = vec.getLength();
        Integer k = N / 2;
        while (k >= 1) {
            for (Integer i = 0; i < k; i++) {
                for (Integer j = k + i; j < N; j += k) {
                    Integer v = vec.get(j);
                    Integer n = j - k;
                    while (n >= 0 && vec.get(n) > v) {
                        vec.update(vec.get(n), n + k);
                        n -= k;
                    }
                    vec.update(v, n + k);
                }
            }
            k /= 2;
        }
    }
}