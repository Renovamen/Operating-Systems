import javax.swing.*;
import java.awt.*;
import java.util.*;

public class elevator extends Thread
{
    private int name;
    private int currentState; //该电梯当前运动状态 停止：0 上升：1 下降：-1
    private int emerState; //紧急状态
    private int currentFloor; //该电梯当前所在层数
    private int maxUp; //该电梯要去的最高楼层
    private int minDown; //该电梯要去的最低楼层

    private Comparator<Integer> cmpUp = new Comparator<Integer>()
    {
        @Override
        public int compare(Integer o1, Integer o2)
        {
            return o1 - o2;
        }
    };
    private Comparator<Integer> cmpDown = new Comparator<Integer>()
    {
        @Override
        public int compare(Integer o1, Integer o2)
        {
            return o2 - o1;
        }
    };
    private Queue<Integer> upStopList = new PriorityQueue<Integer>(15, cmpUp); //下降停止队列
    private Queue<Integer> downStopList = new PriorityQueue<Integer>(15, cmpDown); //上升停止队列
    private JButton[] buttonList; //按钮队列（ui）

    elevator(int name, int dir, JButton[] buttonList)
    {
        this.name = name;
        maxUp = 0;
        minDown = 19;
        currentState = dir;
        currentFloor = 0;
        emerState = -1;
        this.buttonList = buttonList;
    }

    public int getCurrentState() //获取该电梯现在的状态
    {
        return currentState;
    }

    public void setCurrentState(int currentState) //修改该电梯现在的状态
    {
        if(currentState == -2) emerState = this.currentState;
        if(currentState == 2)
        {
            currentState = emerState;
            emerState = -1;
        }
        this.currentState = currentState;
    }

    public int getCurrentFloor() //获取该电梯当前所在楼层
    {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor)
    {
        this.currentFloor = currentFloor;
    }

    public void popUp()
    {
        upStopList.poll();
    }

    public void addUp(Integer pos)
    {
        upStopList.add(pos);
    }

    public void popDown(Integer pos)
    {
        downStopList.poll();
    }

    public void addDown(Integer pos)
    {
        downStopList.add(pos);
    }

    public int upMax()
    {
        return maxUp;
    }

    public void setMaxUp(int maxUp)
    {
        this.maxUp = maxUp;
    }

    public int downMin()
    {
        return minDown;
    }

    public void setMinDown(int minDown)
    {
        this.minDown = minDown;
    }

    public void run()
    {
        while(true)
        {
            // 上升状态
            while (currentState == 1)
            {
                boolean blueFlag = false;
                for (int i = 2; i < 20; i++) buttonList[i].setText("上");

                // 上下客
                if (!upStopList.isEmpty() && currentFloor  == upStopList.peek())
                {
                    while (currentFloor  == upStopList.peek())
                    {
                        Integer a = upStopList.poll();
                        ui.logs.append("电梯" + name + "在第" + (currentFloor + 1) + "楼" + "停下\n");
                        ui.logs.append("电梯" + name + "开门\n");
                        buttonList[1].setBackground(Color.YELLOW);
                        buttonList[1].setText("开门");
                        if(upStopList.isEmpty()) break;
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }

                // 载上当前上升的人
                while (!ui.upqueLock[currentFloor]);
                ui.upqueLock[currentFloor] = false;
                if (!ui.upqueue[currentFloor].isEmpty()) //该楼层上升请求队列不为空
                {
                    for (int i = 0; i < ui.upqueue[currentFloor].size(); i++)
                    {
                        if ((int) ui.upqueue[currentFloor].get(i) - 1 > maxUp) maxUp = (int) ui.upqueue[currentFloor].get(i) - 1; //更改该电梯要去的最高楼层
                        addUp((Integer) ui.upqueue[currentFloor].get(i) - 1); //将请求加入该电梯上升停止队列
                        ui.logs.append("电梯" + name + "在第" + (currentFloor + 1) + "楼载上去" + ui.upqueue[currentFloor].get(i) + "楼的乘客\n");
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }
                ui.upqueue[currentFloor].clear(); //清空该楼层上升请求队列
                ui.upqueLock[currentFloor] = true;

                // 电梯走空 载上向下的人
                while (!ui.downqueLock[currentFloor]);
                ui.downqueLock[currentFloor] = false;
                if (upStopList.isEmpty() && !ui.downqueue[currentFloor].isEmpty()) //该楼层上升停止队列为空且下降请求队列不为空
                {
                    for (int i = 0; i < ui.downqueue[currentFloor].size();i++)
                    {
                        if ((int)ui.downqueue[currentFloor].get(i) - 1 < minDown) minDown = (int)ui.downqueue[currentFloor].get(i) - 1; //更改该电梯要去的最低楼层
                        addDown((Integer) ui.downqueue[currentFloor].get(i) - 1); //将请求加入该电梯下降停止队列
                        ui.logs.append("电梯" + name + "在第" + (currentFloor + 1) + "楼载上去" + ui.downqueue[currentFloor].get(i) + "楼的乘客\n");
                    }

                    if (!downStopList.isEmpty()) //该电梯下降停止队列不为空 则输出下降信息
                    {
                        ui.downqueue[currentFloor].clear(); //清空该楼层下降请求队列
                        setCurrentState(-1); //更改该电梯当前状态为下降
                        blueFlag = true;
                        ui.downqueLock[currentFloor] = true;

                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        buttonList[1].setText("关门");
                        ui.logs.append("电梯" + name + "关门\n");
                        buttonList[1].setBackground(Color.WHITE);
                        ui.logs.append("电梯" + name + "开始下降\n");
                        break;
                    }
                }
                ui.downqueLock[currentFloor] = true;

                if (blueFlag) //停顿
                {
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    buttonList[currentFloor].setBackground(Color.RED);
                    buttonList[1].setText("关门");
                    ui.logs.append("电梯" + name + "关门\n");
                    buttonList[1].setBackground(Color.WHITE);
                }

                // 电梯空了 到顶了
                if (upStopList.isEmpty() || currentFloor == 19)
                {
                    setCurrentState(0); //修改该电梯状态为停止
                    maxUp = 0;
                    minDown = 19;
                    buttonList[currentFloor].setBackground(Color.RED);
                    ui.logs.append("电梯" + name + ": 停止\n");
                    break;
                }

                while(buttonList[1].getText().equals("开门"))
                {
                    try
                    {
                        Thread.sleep(700);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                };

                buttonList[currentFloor].setBackground(Color.WHITE);
                currentFloor++; //上一层红灯亮
                buttonList[currentFloor].setBackground(Color.RED);

                try
                {
                    Thread.sleep(700);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            // 下降状态
            while(currentState == -1)
            {
                boolean blueFlag = false;
                for (int i = 2; i < 20; i++) buttonList[i].setText("下");

                // 上下客
                if (!downStopList.isEmpty() && currentFloor  == downStopList.peek())
                {
                    System.out.println(downStopList.peek());
                    while (currentFloor  == downStopList.peek())
                    {
                        Integer a = downStopList.poll();
                        ui.logs.append("电梯" + name + "在第" + (currentFloor + 1) + "楼" + "停下\n");
                        ui.logs.append("电梯" + name + "开门\n");
                        buttonList[1].setBackground(Color.YELLOW);
                        buttonList[1].setText("开门");
                        if(downStopList.isEmpty()) break;
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }

                // 载上当前下降的人
                while (!ui.downqueLock[currentFloor]);
                ui.downqueLock[currentFloor] = false;
                if (!ui.downqueue[currentFloor].isEmpty())
                {
                    for (int i = 0; i < ui.downqueue[currentFloor].size(); i++)
                    {
                        if ((int) ui.downqueue[currentFloor].get(i) - 1 < minDown) minDown = (int) ui.downqueue[currentFloor].get(i) - 1;
                        addDown((Integer) ui.downqueue[currentFloor].get(i) - 1);
                        ui.logs.append("电梯" + name + "在第" + (currentFloor + 1) + "楼载上去" + ui.downqueue[currentFloor].get(i) + "楼的乘客\n");
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }
                ui.downqueue[currentFloor].clear();
                ui.downqueLock[currentFloor] = true;

                // 电梯走空 载上向上的人
                while (!ui.upqueLock[currentFloor]);
                ui.upqueLock[currentFloor] = false;
                if (downStopList.isEmpty() && !ui.upqueue[currentFloor].isEmpty())
                {
                    for (int i = 0; i < ui.upqueue[currentFloor].size();i++)
                    {
                        if ((int)ui.upqueue[currentFloor].get(i) - 1 > maxUp) maxUp = (int)ui.upqueue[currentFloor].get(i) - 1;
                        addUp((Integer) ui.upqueue[currentFloor].get(i) - 1);
                        ui.logs.append("电梯" + name + "在第" + (currentFloor + 1) + "楼载上去" + ui.upqueue[currentFloor].get(i) + "楼的乘客\n");
                    }

                    if (!upStopList.isEmpty())
                    {
                        ui.upqueue[currentFloor].clear();
                        setCurrentState(1);
                        blueFlag = true;
                        ui.upqueLock[currentFloor] = true;

                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        buttonList[1].setText("关门");
                        ui.logs.append("电梯" + name + "关门\n");
                        buttonList[1].setBackground(Color.WHITE);
                        ui.logs.append("电梯" + name + "开始上升\n");
                        break;
                    }
                }
                ui.upqueLock[currentFloor] = true;

                if (blueFlag) //停顿
                {
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    buttonList[currentFloor].setBackground(Color.RED);
                    buttonList[1].setText("关门");
                    ui.logs.append("电梯" + name + "关门\n");
                    buttonList[1].setBackground(Color.WHITE);
                }

                // 电梯走空 到底
                if (downStopList.isEmpty() || currentFloor == 0)
                {
                    buttonList[currentFloor].setBackground(Color.RED);
                    setCurrentState(0);
                    maxUp = 0;
                    minDown = 19;
                    ui.logs.append("电梯" + name + "停止\n");
                    break;
                }

                while(buttonList[1].getText().equals("开门"))
                {
                    try
                    {
                        Thread.sleep(700);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                };

                buttonList[currentFloor].setBackground(Color.WHITE);
                currentFloor--;
                buttonList[currentFloor].setBackground(Color.RED);
                try
                {
                    Thread.sleep(700);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }

            // 停止
            while(currentState == 0)
            {
                for (int i = 2; i < 20; i++) buttonList[i].setText("-");
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            // 防止线程阻塞
            try
            {
                Thread.sleep(20);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
