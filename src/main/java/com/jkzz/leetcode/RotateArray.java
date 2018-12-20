package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description: 旋转数组
 * @author: 周振
 * @create: 2018-12-20 21:50
 *
 *
 **/
public class RotateArray {
    /**
     * 我们可以发现旋转是有周期性的，周期是数组的长度，所以数组反转的次数是可控制的，只需旋转k对数组长度取余数次即可。
     最直接的办法就是使用两层循环，外层控制旋转次数，内层控制元素交换位置。
     此解法时间复杂度为O(n*k)，空间复杂度为O(1)。
     * @param nums
     * @param k
     */
    public void rotate(int[] nums, int k) {
        if (k % nums.length != 0) {
            int pre, tem;
            for (int i=0; i < k % nums.length; i++) {
                pre = nums[nums.length-1];
                for (int j=0; j<nums.length; j++) {
                    tem = nums[j];
                    nums[j] = pre;
                    pre = tem;
                }
            }
        }
    }


    /**
     * 新建一个大小和nums一样的数组，然后将旋转后的元素放入新的数组中，再遍历新数组赋值给nums。新数组的元素从nums的长度先减去k再加上i开始，直到i不小于k为止，这些元素是需要拿到数组前面去的。当i大于等于k时，新数组的元素要从nums的第1位开始，也就是将原数组前面的元素放到后面来。

     对于k的值，先判断对数组长度取余是否等于0，不等于0后才会开始下一步操作。
     此解法的时间复杂度是O(n)，空间复杂度是O(n)，因为使用了新的数组。
     * @param nums
     * @param k
     */
    public void rotate2(int[] nums, int k) {
        if (k % nums.length != 0) {
            k = k % nums.length;
            int pointer = 0;
            int[] tem = new int[nums.length];
            for (int i=0; i < tem.length; i++) {
                if (i<k) {
                    tem[i] = nums[tem.length-k+i];
                } else {
                    tem[i] = nums[pointer++];
                }
            }
            for (int j=0; j<nums.length; j++) {
                nums[j] = tem[j];
            }
        }
    }

    /**
     *先通过一个小例子来说明：

     nums = {1,2,3,4,5,6,7}，k = 3，

     先反转其全部元素，变成{7,6,5,4,3,2,1}

     再反转前3个元素，变成{5,6,7,4,3,2,1}

     再反转后4个元素，变成{5,6,7,1,2,3,4}

     这样就能够得到答案了。
     此解法的时间复杂度是O(n)，空间复杂度是O(1)。
     * @param nums
     * @param k
     */
    public void rotate3(int[] nums, int k) {
        if (k % nums.length != 0) {
            k %= nums.length;
            reverse(nums, 0, nums.length - 1);
            reverse(nums, 0, k - 1);
            reverse(nums, k, nums.length - 1);
        }
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

}
