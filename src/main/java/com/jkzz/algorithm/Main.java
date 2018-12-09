package com.jkzz.algorithm;

import java.util.Scanner;


/**
 * 机器人棋盘问题
 */

public class Main {


    /**
     * 递归返回从（0,0）到（m，n）的路线数,以左上角为原点建立平面直角坐标系,
     *
     * @param m 行数
     * @param n 列数
     * @return count
     */
    private static int getRoutesRecursive(int m, int n) {
        if (m == 0 && n == 0) {//底部无路可走
            return 0;
        }
        if (m == 0 && n == 1) {//右移一步
            return 1;
        }
        if (m == 1 && n == 0) {//下移一步
            return 1;
        }
        if (m == 0) {//底部，向右移
            return getRoutesRecursive(m, n - 1);
        }
        if (n == 0) {//右侧,向下移
            return getRoutesRecursive(m - 1, n);
        } else {//递归，下移路线数加右移路线数
            return getRoutesRecursive(m - 1, n) + getRoutesRecursive(m, n - 1);
        }
    }

    /**
     * 动态规划返回从（0,0）到（m，n）的路线数
     *
     * @param m 行数
     * @param n 列数
     * @return count
     */
    private static int getRoutesDynamic(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];//dp[i][j]代表从（0,0）到（m，n）的路线数
        for (int i = 1; i <= m; i++) {
            dp[i][0] = 1;//初始化,左侧路线为1， 只需下移
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = 1;//初始化,右侧路线为1, 只需右移
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];//状态转移方程:下移路线数加右移路线数
            }
        }

        return dp[m][n];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        int n = scanner.nextInt();

        if (m < 0 || n < 0) {
            System.err.println("illegal num format");
        } else {
            int count1 = getRoutesDynamic(m, n);
            int count2 = getRoutesRecursive(m, n);
            System.out.println(count1);
            System.out.println(count1 == count2);
        }
    }
}

