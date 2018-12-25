package com.jkzz.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: jkzzDemo
 * @description: 两数相加
 * @author: 周振
 * @create: 2018-12-25 17:03
 **/
public class TwoSum {


    public static int[] twoSum1(int[] nums, int target) {
        HashMap<Integer, Integer> tracker = new HashMap<Integer, Integer>();
        int len = nums.length;
        for(int i = 0; i < len; i++){
            if(tracker.containsKey(nums[i])){
                int left = tracker.get(nums[i]);
                return new int[]{left, i};
            }else{
                tracker.put(target - nums[i], i);
            }
        }
        return new int[2];
    }
    public static int[] twoSum(int[] numbers, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            if (map.containsKey(target - numbers[i])) {
                result[1] = i + 1;
                result[0] = map.get(target - numbers[i]);
                return result;
            }
            map.put(numbers[i], i + 1);
        }
        return result;
    }

    public static void main(String[] args) {
int[] a={1,2,3,4};
        int[] ints = twoSum(a, 5);
        for (int c:ints) {
            System.out.println(c);
        }
    }
}
