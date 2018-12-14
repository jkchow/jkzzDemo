package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description: 移动0 往后，不改变原有顺序
 * @author: 周振
 * @create: 2018-12-14 10:17
 **/
public class MoveZeroes {
    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length == 0) return;

        int insertPos = 0;
        for (int num : nums) {
            if (num != 0) nums[insertPos++] = num;
        }

        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }

    public void moveZeroes0(int[] nums) {

        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                int temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                j++;
            }
        }
    }

    public void moveZeroes1(int[] nums) {
        int n = nums.length;
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                m++;
            } else if (m != 0) {
                nums[i - m] = nums[i];
                nums[i] = 0;
            }
        }

    }
}
