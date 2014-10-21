package dynamic_programming;

import java.util.Scanner;

/**
 * 求最长公共子序列长度 以及对应子序列
 * 
 * 若给定序列 ABCDEFG  , 则 ABC , CDE, ABCDEFG 等都是其自序列
 * 
 * @author LiangGe
 *
 */
public class LCSQuestion {
	
	/**
	 * 计算最长公共子序列
	 * 由于在所考虑的子问题空间中，总共只有θ(m*n)个不同的子问题，因此，用动态规划算法自底向上地计算最优值能提高算法的效率
	 * 使用此方法每个子问题都会计算
	 * 
	 * 时间复杂度为0(M*N)
	 */
	public static int getLcsLength(String str_x, String str_y, int lcsLengthArray[][], int lcsLengthMap[][]) {
		int len_x = str_x.length();
		int len_y = str_y.length();
		// 边界，X字符串为空
		for(int i=0; i<=len_x; i++) {
			lcsLengthArray[i][0] = 0;
		}
		// 边界，Y字符串为空
		for(int j=0; j<=len_y; j++) {
			lcsLengthArray[0][j] = 0;
		}
		
		for(int i=1; i<=len_x; i++) {
			for(int j=1; j<=len_y; j++) {
				// 母问题对应三个子问题，求出三个子问题各自的最优解，再判断母问题最优解
				if(str_x.charAt(i-1) == str_y.charAt(j-1)) {
					lcsLengthArray[i][j] = lcsLengthArray[i-1][j-1] + 1;
					lcsLengthMap[i][j] = 1;
				} else {
					if(lcsLengthArray[i-1][j] >= lcsLengthArray[i][j-1]) {
						lcsLengthArray[i][j] = lcsLengthArray[i-1][j];
						lcsLengthMap[i][j] = 2;
					} else {
						lcsLengthArray[i][j] = lcsLengthArray[i][j-1];
						lcsLengthMap[i][j] = 3;
					}
				}
			}
		}
		return lcsLengthArray[len_x][len_y];
 	}
	
	/**
	 * 利用递归计算最长公共子序列,但是由于计算过的子问题会做备忘录，所以不会出现重复计算问题
	 * 使用此方法 不是所有子问题都需要计算一遍，只有具体问题需要用到的时候才会计算子问题
	 * 
	 */
	public static int getLcsLength2(String str_x, String str_y, int len_x, int len_y, int lcsLengthArray[][], int lcsLengthMap[][]) {
		int curLcsLength;
		if(lcsLengthArray[len_x][len_y] != 0) {
			curLcsLength = lcsLengthArray[len_x][len_y];
		// 边界问题 len_x = 0 ||  len_y = 0	
		} else if(len_x == 0 || len_y == 0) {
			lcsLengthArray[len_x][len_y] = 0;
			curLcsLength = 0;
		} else if(str_x.charAt(len_x-1) == str_y.charAt(len_y-1)) {
			curLcsLength = getLcsLength2(str_x, str_y, len_x-1, len_y-1, lcsLengthArray, lcsLengthMap) + 1;
			lcsLengthMap[len_x][len_y] = 1;
		} else {
			int len1 = getLcsLength2(str_x, str_y, len_x-1, len_y, lcsLengthArray, lcsLengthMap);
			int len2 = getLcsLength2(str_x, str_y, len_x, len_y-1, lcsLengthArray, lcsLengthMap);
			if(len1 >= len2) {
				curLcsLength = len1;
				lcsLengthMap[len_x][len_y] = 2;
			} else {
				curLcsLength = len2;
				lcsLengthMap[len_x][len_y] = 3;
			}
		}
		lcsLengthArray[len_x][len_y] = curLcsLength;
		return curLcsLength;
	}
	
	/**
	 * 逆向求解具体最长子序列
	 */
	public static String getLcsString(int lcsLength, String str_x, String str_y, int lcsLengthMap[][]) {
		int i = str_x.length();
		int j = str_y.length();
		char str[] = new char[lcsLength];
		
		for(int k=lcsLength-1; k>=0;) {
			if(lcsLengthMap[i][j] == 1) {
				str[k--] = str_x.charAt(i-1);
				i--; j--;
			} else if(lcsLengthMap[i][j] == 2) {
				i --;
			} else {
				j --;
			}
		}
		
		return String.valueOf(str);
	}
	
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int lcsLengthArray[][];
		int lcsLengthMap[][];
		String lcsString;
		int lcsLength;
		while(scanner.hasNext()) {
			String curStrLine = scanner.nextLine();
			String str[] = curStrLine.split(" ");
			String str_x = str[0];
			String str_y = str[1];
			
			lcsLengthArray = new int[str_x.length()+1][str_y.length()+1];
			lcsLengthMap = new int[str_x.length()+1][str_y.length()+1];
/*			// 非递归求解
			lcsLength = getLcsLength(str_x, str_y, lcsLengthArray, lcsLengthMap);
			System.out.println("lcsLength ： " + lcsLength);
			
			lcsString = getLcsString(lcsLength, str_x, str_y, lcsLengthMap);
			System.out.format("%s\n", lcsString);*/
			
			// 递归求解
			int len_x = str_x.length();
			int len_y = str_y.length();
			lcsLength = getLcsLength2(str_x, str_y, len_x, len_y, lcsLengthArray, lcsLengthMap);
			System.out.println("lcsLength ： " + lcsLength);
			
			lcsString = getLcsString(lcsLength, str_x, str_y, lcsLengthMap);
			System.out.format("%s\n", lcsString);
		}
	}
}
