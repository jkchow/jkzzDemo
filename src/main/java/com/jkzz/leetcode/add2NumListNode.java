package com.jkzz.leetcode;

import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.util.Stack;


public class add2NumListNode {

    public static void main(String[] args) {
        int[] arr = new int[]{4, 5};
        int[] arr2 = new int[]{5, 5, 4, 5};
        ListNode listNode1 = deleteListNode.buildListNode(arr);
        ListNode listNode2 = deleteListNode.buildListNode(arr2);
        ListNode listNode = addTwoNumbers(listNode1, listNode2);
        ListNode listNode5 = addTwoNumbers2(listNode1, listNode2);
        ListNode listNode6 = addTwoNumbers3(listNode1, listNode2);
        System.out.println(listNode5.val);
        System.out.println(listNode6.val);
        System.out.println(listNode6.val);
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

    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode c1 = l1;
        ListNode c2 = l2;
        ListNode sentinel = new ListNode(0);
        ListNode d = sentinel;
        int sum = 0;
        while (c1 != null || c2 != null) {
            sum /= 10;
            if (c1 != null) {
                sum += c1.val;
                c1 = c1.nextNode;
            }
            if (c2 != null) {
                sum += c2.val;
                c2 = c2.nextNode;
            }
            d.nextNode = new ListNode(sum % 10);
            d = d.nextNode;
        }
        if (sum / 10 == 1) {
            d.nextNode = new ListNode(1);
        }
        return sentinel.nextNode;
    }

    public static ListNode addTwoNumbers3(ListNode l1, ListNode l2) {
        ListNode prev = new ListNode(0);
        ListNode head = prev;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            ListNode cur = new ListNode(0);
            int sum = ((l2 == null) ? 0 : l2.val) + ((l1 == null) ? 0 : l1.val) + carry;
            cur.val = sum % 10;
            carry = sum / 10;
            prev.nextNode = cur;
            prev = cur;

            l1 = (l1 == null) ? l1 : l1.nextNode;
            l2 = (l2 == null) ? l2 : l2.nextNode;
        }
        return head.nextNode;
    }
}
