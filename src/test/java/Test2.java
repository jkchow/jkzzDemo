public class Test2 {

    public static void main(String[] args){
        int a [] = {5,4,9,1,7,6,2,3,8};
        quickSort(a,0,8);
        for(int i=0;i<9;i++)
        {
            System.out.println(a[i]);
        }

    }

    public static void  quickSort(int a [], int left,int right){
        if(left < right){
            int p = partition(a,left,right);
            quickSort(a,left,p-1);
            quickSort(a,p+1,right);
        }
    }

    public static int partition(int a [],int left ,int right){
        int temp = a[left];//最左边的元素作为基准点
        while (left < right){
            while (left < right && a[right] >= temp)
                right--;
            if(left < right) {
                a[left] = a[right];
            }

            while (left < right && a[left] <= temp)
                left++;
            if(left < right){
                a[right] = a[left];
            }
        }

        a[left] = temp;

        return left;
    }
}
