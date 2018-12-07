package com.jkzz.leetcode;

import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.util.Stack;


public class add2NumListNode {

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 3, 4};
        int[] arr2 = new int[]{3, 4, 4, 5};
        ListNode listNode1 = deleteListNode.buildListNode(arr);
        ListNode listNode2 = deleteListNode.buildListNode(arr2);
        ListNode listNode = addTwoNumbers(listNode1, listNode2);
        System.out.println(listNode.val);
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        while (l1 != null) {
            s1.push(l1.val);
            l1 = l1.nextNode;
        }
        while (l2 != null) {
            s2.push(l2.val);
            l2 = l2.nextNode;
        }
        int sum = 0;
        ListNode list = new ListNode(0);
        while (!s1.empty() || !s2.empty()) {
            if (!s1.empty()) sum += s1.pop();
            if (!s2.empty()) sum += s2.pop();
            list.val = sum % 10;
            ListNode head = new ListNode(sum / 10);
            head.nextNode = list;
            list = head;
            sum /= 10;
        }
        return list.val == 0 ? list.nextNode : list;
    }
}
