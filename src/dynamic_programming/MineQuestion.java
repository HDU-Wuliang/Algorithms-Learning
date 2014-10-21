package dynamic_programming;

import java.util.Scanner;

/**
 * 给定一定人数peopleNum，以及一定金矿数目mineNum
 * 接下来分别输入每个金矿开采  所需人数peopleNeed 和  该金矿开采后  可得多少黄金goldValue
 * 计算可获取最大黄金value
 * 
 * @author LiangGe
 *
 * 测试数据:
100 5
77 92
22 22
29 87
50 46
99 90
 */
public class MineQuestion {
	
	/**
	 * 通过使用二维数据求解 每个问题规模
	 * 该方法很多无需计算的问题计算了
	 * 
	 * 时间复杂度为 0(总人数*金矿数)
	 */
	public static int calculateMaxGoldValue(int peopleTotal, int mineNum, int peopleNeed[], int goldValue[], int maxGoldVlueMatrix[][], int maxGoldMap[][]) {
		// 人数为0 时全部金矿表示为0，不可能有金矿
		for(int j=0; j<mineNum; j++) {
			maxGoldVlueMatrix[0][j] = 0;
		}
		// 第一个金矿，作为判断的边界
		for(int i=1; i<= peopleTotal; i++) {
			if(i >= peopleNeed[1]) {
				maxGoldVlueMatrix[i][1] = goldValue[1];
				maxGoldMap[i][1] = 2;
			} else {
				maxGoldVlueMatrix[i][1] = 0;
				maxGoldMap[i][0] = 1;
			}
		}
		// 第一个以外的金矿
		for(int i=1; i<= peopleTotal; i++) {
			for(int j=2; j<= mineNum; j++) {
				// 根据转移方程 f(peopleNum, mineNum) = max[ f(peopleNum, mineNum-1), f(peopleNum-peopleNeed[j], mineNum-1)+goldVlue[j]]
				// 求当前最优解的结果 取决于两个子问题的最优解比较
				// f(peopleNum-peopleNeed[j], mineNum-1)+goldVlue[j] 子问题为 选择当前金矿，但是前提是当前总人数能提供给 金矿所需人数.
				
				// if语句当前总人数能提供给 金矿所需人数，如果不能提供，则当前金矿无需选择，直接选择另外一个子问题的结果即可
				// else 如果当前人数能够提供，则还需要判断两个子问题各自的最优解，再求出母问题的最优解
				if(i < peopleNeed[j] || (i - peopleNeed[j]) < 0) {
					maxGoldVlueMatrix[i][j] = maxGoldVlueMatrix[i][j-1];
					// 标记选择转移方程中第一种，即当前金矿没有选择
					maxGoldMap[i][j] = 1;
				} else {
					if(maxGoldVlueMatrix[i][j-1] >= (maxGoldVlueMatrix[i-peopleNeed[j]][j-1] + goldValue[j])) {
						maxGoldVlueMatrix[i][j] = maxGoldVlueMatrix[i][j-1];
						// 标记选择转移方程中第一种，即当前金矿没有选择
						maxGoldMap[i][j] = 1;
					} else {
						maxGoldVlueMatrix[i][j] = maxGoldVlueMatrix[i-peopleNeed[j]][j-1] + goldValue[j];
						// 标记选择转移方程中第二种，即选择当前金矿
						maxGoldMap[i][j] = 2;
					}
				}
			}
		}
		
		return maxGoldVlueMatrix[peopleTotal][mineNum];
	}
	
	
	/**
	 * 使用递归的方式计算，但是由于计算过的子问题会做备忘录，所以不会出现重复计算问题
	 * 而且使用该方法能够避免不需要计算的问题
	 * 也就是二维数组中只计算相关的子问题
	 */
	public static int calculateMaxGoldValue2(int peopleTotal, int mineNum, int peopleNeed[], int goldValue[], int maxGoldVlueMatrix[][], int maxGoldMap[][]) {
		int curMaxGoldValue = 0;
		int step1, step2;
		// 如果问题计算过,[对应动态规划中的“做备忘录”]
		if(maxGoldVlueMatrix[peopleTotal][mineNum] != 0) {
			curMaxGoldValue = maxGoldVlueMatrix[peopleTotal][mineNum];
		// 如果仅有一个金矿时 [对应动态规划中的“边界”]
		} else if(mineNum == 1) {
			// 当给出的人数足够开采这座金矿
			if(peopleTotal > peopleNeed[mineNum]) {
				// 得到的最大值就是这座金矿的金子数
				curMaxGoldValue = goldValue[mineNum];
				maxGoldMap[peopleTotal][mineNum] = 2;
			} else {
				// 否则这唯一的一座金矿也不能开采
				curMaxGoldValue = 0;
				maxGoldMap[peopleTotal][mineNum] = 1;
			}
		// 如果给出的人够开采这座金矿 [对应动态规划中的“最优子结构”]	
		} else if(peopleTotal >= peopleNeed[mineNum]) {
			// 考虑当前金矿开采与不开采两种情况，取最大值
			step1 = calculateMaxGoldValue2(peopleTotal, mineNum-1, peopleNeed, goldValue, maxGoldVlueMatrix, maxGoldMap);
			step2 = calculateMaxGoldValue2(peopleTotal-peopleNeed[mineNum], mineNum-1, peopleNeed, goldValue, maxGoldVlueMatrix, maxGoldMap);
			if(step1 >= (step2+goldValue[mineNum])) {
				curMaxGoldValue = step1;
				maxGoldMap[peopleTotal][mineNum] = 1;
			} else {
				curMaxGoldValue = step2 + goldValue[mineNum];
				maxGoldMap[peopleTotal][mineNum] = 2;
			}
		} else {
			// 否则给出的人不够开采这座金矿 [对应动态规划中的“最优子结构”]
			// 仅考虑不开采的情况
			step1 = calculateMaxGoldValue2(peopleTotal, mineNum-1, peopleNeed, goldValue, maxGoldVlueMatrix, maxGoldMap);
			curMaxGoldValue = step1;
			maxGoldMap[peopleTotal][mineNum] = 1;
		}
		// 做备忘录   
		maxGoldVlueMatrix[peopleTotal][mineNum] = curMaxGoldValue;
		return curMaxGoldValue;
	}
	
	/**
	 * 打印出具体选择哪些金矿
	 */
	public static void printChosenMine(int peopleTotal, int mineNum, int peopleNeed[], int maxGoldMap[][]) {
		System.out.println("金矿选择情况如下：");
		boolean mineChosen[] = new boolean[mineNum + 1];
		int j = peopleTotal;
		for(int i=mineNum; i>=1; i--) {
			if(maxGoldMap[j][i] == 1) {
				mineChosen[i] = false;
			} else if(maxGoldMap[j][i] == 2){
				mineChosen[i] = true;
				j -= peopleNeed[i];
			}
		}
		
		for(int i=0; i<mineNum; i++) {
			if(mineChosen[i]) {
				System.out.format("%3d", i);
			}
		}
	}
	
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int peopleNeed[];
		int goldValue[];
		int maxGoldVlueMatrix[][];
		int maxGoldMap[][];
		int maxGoldValue = 0;
		while(scanner.hasNext()) {
			int peopleTotal = scanner.nextInt();
			int mineNum = scanner.nextInt();
			scanner.nextLine();
			if(peopleTotal == 0 && mineNum == 0) {
				break;
			}
			peopleNeed = new int[mineNum + 1];
			goldValue = new int[mineNum + 1];
			for(int i=1; i<=mineNum; i++) {
				peopleNeed[i] = scanner.nextInt();
				goldValue[i] = scanner.nextInt();
				scanner.nextLine();
			}
			maxGoldVlueMatrix = new int[peopleTotal + 1][mineNum + 1];
			maxGoldMap = new int[peopleTotal + 1][mineNum + 1];

/*			maxGoldValue = calculateMaxGoldValue(peopleTotal, mineNum, peopleNeed, goldValue, maxGoldVlueMatrix, maxGoldMap);
			
			System.out.println("maxGoldValue = " + maxGoldValue);
			
			printChosenMine(peopleTotal, mineNum, peopleNeed, maxGoldMap);*/
						
			maxGoldValue = calculateMaxGoldValue2(peopleTotal, mineNum, peopleNeed, goldValue, maxGoldVlueMatrix, maxGoldMap);
			System.out.println("maxGoldValue = " + maxGoldValue);
			
			printChosenMine(peopleTotal, mineNum, peopleNeed, maxGoldMap);
		}
	}
}
