import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ui
{
    public static JLabel[] labels = new JLabel[20]; //楼层标签
    public static JComboBox[] up = new JComboBox[20]; //上升选择键
    public static JComboBox[] down = new JComboBox[20]; //下降选择键
    public static JButton[] elev1 = new JButton[20], elev2 = new JButton[20], elev3 = new JButton[20], elev4 = new JButton[20], elev5 = new JButton[20];
    public static elevator one, two, three, four, five; //分别为5部电梯
    public static TextArea logs = new TextArea();

    public static ArrayList<elevator> elevators = new ArrayList<elevator>();
    public static ArrayList[] upqueue = new ArrayList[20], downqueue = new ArrayList[20]; //上升和下降请求队列
    public static boolean[] upqueLock = new boolean[20], downqueLock = new boolean[20];

    private static String Help =
            "<html><h2>Help：</h2>"+
            "<li>点击左侧选择框将出现下拉条，可选择要去的楼层，发出请求的的楼层将显示绿色。</li>" +
            "<li>电梯运行中所在楼层将显示红色，上下乘客时将显示蓝色并停顿</li>" +
            "<li>电梯上行时3-20层将显示“上”，电梯下行时3-20层将显示“下”</li>" +
            "<li>点击1楼的按钮可控制电梯正常或故障,初始为正常，点击一次将使电梯故障,再点一次恢复正常</li>" +
            "<li>点击2楼的按钮可控制电梯开门或关门,初始为关门，点击一次将使电梯开门,再点一次关门</li>"+
            "<li>祝好</li></html>";


    public static void init()
    {
        for (int i = 0; i < 20; i++)
        {
            // 队列锁初始化
            upqueLock[i] = true;
            downqueLock[i] = true;

            // 楼号初始化
            labels[i] = new JLabel(String.valueOf(i + 1));
            labels[i].setBackground(Color.WHITE);
            labels[i].setOpaque(true);

            // 请求队列初始化
            // 上升等待队列
            upqueue[i] = new ArrayList<Integer>();
            // 下降等待队列
            downqueue[i] = new ArrayList<Integer>();

            // 上升选择键初始化
            up[i] = new JComboBox();
            up[i].addItem("-");
            for(int k = i + 2; k <= 20; k++) up[i].addItem(String.valueOf(k));
            final int finalI = i;
            up[i].addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    if (ItemEvent.SELECTED == e.getStateChange() && !up[finalI].getSelectedItem().toString().equals("-"))
                    {
                        upqueue[finalI].add(Integer.parseInt(up[finalI].getSelectedItem().toString()));
                        labels[finalI].setBackground(Color.GREEN);
                        up[finalI].setSelectedIndex(0);
                        logs.append("第" + (finalI + 1) + "楼有人要去" + upqueue[finalI] + "楼\n");
                    }
                }
            });

            // 下降选择键初始化
            down[i] = new JComboBox();
            down[i].addItem("-");
            for(int k = i; k > 0; k--) down[i].addItem(String.valueOf(k));
            down[i].addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    if (ItemEvent.SELECTED == e.getStateChange() && !down[finalI].getSelectedItem().toString().equals("-"))
                    {
                        downqueue[finalI].add(Integer.parseInt(down[finalI].getSelectedItem().toString()));
                        labels[finalI].setBackground(Color.GREEN);
                        down[finalI].setSelectedIndex(0);
                        logs.append("第" + (finalI + 1) + "楼有人要去" + downqueue[finalI] + "楼\n");
                    }
                }
            });
        }

        // 电梯1初始化
        for (int i = 0; i < 20; i++)
        {
            elev1[i] = new JButton("-");
            elev1[i].setOpaque(true);
            elev1[i].setBackground(Color.WHITE);
        }

        // 故障/正常
        elev1[0].setBackground(Color.RED);
        elev1[0].setText("正常");
        elev1[0].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev1[0].getText().equals("正常"))
                {
                    elev1[0].setText("故障");
                    elev1[0].setBackground(Color.ORANGE);
                    one.setCurrentState(-2);
                    logs.append("电梯1故障,停止运行\n");
                }
                else
                {
                    elev1[0].setText("正常");
                    elev1[0].setBackground(Color.RED);
                    one.setCurrentState(2);
                    logs.append("电梯1恢复\n");
                }
            }
        });

        // 开门/关门
        elev1[1].setText("关门");
        elev1[1].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev1[1].getText().equals("关门"))
                {
                    elev1[1].setText("开门");
                    elev1[1].setBackground(Color.YELLOW);
                    logs.append("电梯1开门\n");
                }
                else
                {
                    elev1[1].setText("关门");
                    elev1[1].setBackground(Color.WHITE);
                    logs.append("电梯1关门\n");
                }
            }
        });


        // 电梯2初始化
        for (int i = 0; i < 20; i++)
        {
            elev2[i] = new JButton("-");
            elev2[i].setOpaque(true);
            elev2[i].setBackground(Color.WHITE);
        }
        elev2[0].setBackground(Color.RED);
        elev2[0].setText("正常");
        elev2[0].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev2[0].getText().equals("正常"))
                {
                    elev2[0].setText("故障");
                    elev2[0].setBackground(Color.ORANGE);
                    two.setCurrentState(-2);
                    logs.append("电梯2故障,停止运行\n");
                }
                else
                {
                    elev2[0].setText("正常");
                    elev2[0].setBackground(Color.RED);
                    two.setCurrentState(2);
                    logs.append("电梯2恢复\n");
                }
            }
        });

        // 开门/关门
        elev2[1].setText("关门");
        elev2[1].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev2[1].getText().equals("关门"))
                {
                    elev2[1].setText("开门");
                    elev2[1].setBackground(Color.YELLOW);
                    logs.append("电梯2开门\n");
                }
                else
                {
                    elev2[1].setText("关门");
                    elev2[1].setBackground(Color.WHITE);
                    logs.append("电梯2关门\n");
                }
            }
        });

        // 电梯3初始化
        for (int i = 0; i < 20; i++)
        {
            elev3[i] = new JButton("-");
            elev3[i].setOpaque(true);
            elev3[i].setBackground(Color.WHITE);
        }
        elev3[0].setBackground(Color.RED);
        elev3[0].setText("正常");
        elev3[0].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev3[0].getText().equals("正常"))
                {
                    elev3[0].setText("故障");
                    elev3[0].setBackground(Color.ORANGE);
                    three.setCurrentState(-2);
                    logs.append("电梯3故障,停止运行\n");
                }
                else
                {
                    elev3[0].setText("正常");
                    elev3[0].setBackground(Color.RED);
                    three.setCurrentState(2);
                    logs.append("电梯3恢复\n");
                }
            }
        });

        // 开门/关门
        elev3[1].setText("关门");
        elev3[1].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev3[1].getText().equals("关门"))
                {
                    elev3[1].setText("开门");
                    elev3[1].setBackground(Color.YELLOW);
                    logs.append("电梯3开门\n");
                }
                else
                {
                    elev3[1].setText("关门");
                    elev3[1].setBackground(Color.WHITE);
                    logs.append("电梯3关门\n");
                }
            }
        });

        // 电梯4初始化
        for (int i = 0; i < 20; i++)
        {
            elev4[i] = new JButton("-");
            elev4[i].setOpaque(true);
            elev4[i].setBackground(Color.WHITE);
        }
        elev4[0].setBackground(Color.RED);
        elev4[0].setText("正常");
        elev4[0].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev4[0].getText().equals("正常"))
                {
                    elev4[0].setText("故障");
                    elev4[0].setBackground(Color.ORANGE);
                    four.setCurrentState(-2);
                    logs.append("电梯4故障,停止运行\n");
                }
                else
                {
                    elev4[0].setText("正常");
                    elev4[0].setBackground(Color.RED);
                    four.setCurrentState(2);
                    logs.append("电梯4恢复\n");
                }
            }
        });

        // 开门/关门
        elev4[1].setText("关门");
        elev4[1].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev4[1].getText().equals("关门"))
                {
                    elev4[1].setText("开门");
                    elev4[1].setBackground(Color.YELLOW);
                    logs.append("电梯4开门\n");
                }
                else
                {
                    elev4[1].setText("关门");
                    elev4[1].setBackground(Color.WHITE);
                    logs.append("电梯4关门\n");
                }
            }
        });

        // 电梯5初始化
        for (int i = 0; i < 20; i++)
        {
            elev5[i] = new JButton("-");
            elev5[i].setOpaque(true);
            elev5[i].setBackground(Color.WHITE);
        }
        elev5[0].setBackground(Color.RED);
        elev5[0].setText("正常");
        elev5[0].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev5[0].getText().equals("正常"))
                {
                    elev5[0].setText("故障");
                    elev5[0].setBackground(Color.ORANGE);
                    five.setCurrentState(-2);
                    logs.append("电梯5故障,停止运行\n");
                }
                else
                {
                    elev5[0].setText("正常");
                    elev5[0].setBackground(Color.RED);
                    five.setCurrentState(2);
                    logs.append("电梯5恢复\n");
                }
            }
        });

        // 开门/关门
        elev5[1].setText("关门");
        elev5[1].addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(elev5[1].getText().equals("关门"))
                {
                    elev5[1].setText("开门");
                    elev5[1].setBackground(Color.YELLOW);
                    logs.append("电梯5开门\n");
                }
                else
                {
                    elev5[1].setText("关门");
                    elev5[1].setBackground(Color.WHITE);
                    logs.append("电梯5关门\n");
                }
            }
        });


        JFrame frame = new JFrame("电梯");
        frame.setLayout(new GridLayout(1, 2));
        GridLayout grid = new GridLayout(21, 8);
        Container c = new Container();
        c.setLayout(grid);

        // 标签
        c.add(new JLabel("楼层"));
        c.add(new JLabel("上"));
        c.add(new JLabel("下"));
        c.add(new JLabel("电梯1"));
        c.add(new JLabel("电梯2"));
        c.add(new JLabel("电梯3"));
        c.add(new JLabel("电梯4"));
        c.add(new JLabel("电梯5"));

        // 按钮
        for (int i = 20; i > 0; i--)
        {
            c.add(labels[i - 1]);
            c.add(up[i - 1]);
            c.add(down[i - 1]);

            c.add(elev1[i - 1]);
            c.add(elev2[i - 1]);
            c.add(elev3[i - 1]);
            c.add(elev4[i - 1]);
            c.add(elev5[i - 1]);
        }

        logs.setEditable(false);
        logs.setFont(new Font("黑体",Font.BOLD,18));
        frame.add(c);
        JScrollPane pane = new JScrollPane(logs);
        frame.add(pane);

        frame.setSize(new Dimension(2000, 1500));
        frame.setVisible(true);

        // 初始化电梯
        one = new elevator(1, 0, elev1);
        elevators.add(one);
        two = new elevator(2, 0, elev2);
        elevators.add(two);
        three = new elevator(3, 0, elev3);
        elevators.add(three);
        four = new elevator(4, 0, elev4);
        elevators.add(four);
        five = new elevator(5, 0, elev5);
        elevators.add(five);

        // help提示框
        JOptionPane.showMessageDialog(null, Help, "Help", JOptionPane.DEFAULT_OPTION);
    }

    static class lightManger extends Thread
    {
        lightManger()
        {
            start();
        }
        public void run()
        {
            while (true)
            {
                for (int i = 0; i < 20; i++)
                    if (upqueue[i].isEmpty() && downqueue[i].isEmpty()) labels[i].setBackground(Color.WHITE);

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

    static class elevatorManager extends Thread
    {
        elevatorManager()
        {
            start();
        }

        public void adjust(int index, int i) throws InterruptedException
        {
            // 最优电梯位于当前楼层下方
            if (elevators.get(index).getCurrentFloor()< i)
            {
                elevators.get(index).setCurrentState(1);
                elevators.get(index).addUp(i);
                elevators.get(index).setMaxUp(i);
                logs.append("电梯" + (index + 1) + "开始上升\n");
                Thread.sleep(700);
                return;
            }

            // 最优电梯位于当前楼层上方
            if (elevators.get(index).getCurrentFloor()> i)
            {
                elevators.get(index).setCurrentState(-1);
                elevators.get(index).addDown(i);
                elevators.get(index).setMinDown(i);
                logs.append("电梯" + (index + 1) + "开始下降\n");
                Thread.sleep(700);
                return;
            }

            // 最优电梯位于当前楼层
            if (elevators.get(index).getCurrentFloor() == i)
            {
                elevators.get(index).setCurrentState(1);
                logs.append("电梯" + (index + 1) + "启动\n");
                Thread.sleep(700);
                return;
            }
        }

        public void run()
        {
            while (true)
            {
                for (int i = 0; i < 20; i++)
                {
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    while (!upqueLock[i]);
                    if (!upqueue[i].isEmpty()) //i楼上升请求队列不为空
                    {
                        int  selectid= -1,  distance = 1000000;

                        //检索可用且离i楼最近的电梯
                        for (int k = 0; k < 5; k++)
                        {
                            //电梯k停止
                            if (elevators.get(k).getCurrentState() == 0 && !upqueue[i].isEmpty())
                            {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance)
                                {
                                    selectid = k;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }

                            //电梯k所在楼层高于等于i楼+电梯k为下降状态+电梯k要去的最低楼层高于等于i
                            if (elevators.get(k).getCurrentFloor() >= i && elevators.get(k).getCurrentState() == -1 && elevators.get(k).downMin() >= i)
                            {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance)
                                {
                                    selectid = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }

                            //电梯k所在楼层低于等于i楼+电梯k为上升状态+电梯k要去的最高楼层高于等于i
                            if (elevators.get(k).getCurrentFloor() <= i && elevators.get(k).getCurrentState() == 1 && elevators.get(k).upMax() >= i)
                            {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance)
                                {
                                    selectid = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                        }

                        if (selectid != -1 && !upqueue[i].isEmpty()) //有可用电梯
                        {
                            try
                            {
                                adjust(selectid, i);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                for (int i = 0; i < 20; i++)
                {
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }


                    while (!downqueLock[i]);
                    if (!downqueue[i].isEmpty()) //i楼下降请求队列不为空
                    {
                        int index = -1,  distance = 1000000;
                        //检索可用且离i楼最近的电梯
                        for (int k = 0; k < 5; k++)
                        {
                            //电梯k停止
                            if (elevators.get(k).getCurrentState() == 0 && !downqueue[i].isEmpty())
                            {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance)
                                {
                                    index = k;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }

                            //电梯k所在楼层高于等于i楼+电梯k为下降状态+电梯k要去的最低楼层低于等于i
                            if (elevators.get(k).getCurrentFloor() >= i && elevators.get(k).getCurrentState() == -1 && elevators.get(k).downMin() <= i){
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance)
                                {
                                    index = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }

                            //电梯k所在楼层低于等于i楼+电梯k为上升状态+电梯k要去的最高楼层低于等于i
                            if (elevators.get(k).getCurrentFloor() <= i && elevators.get(k).getCurrentState() == 1 && elevators.get(k).upMax() <= i){
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance)
                                {
                                    index = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                        }

                        if (index != -1 && !downqueue[i].isEmpty())
                        {
                            try
                            {
                                adjust(index, i);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                try
                {
                    Thread.sleep(700);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args)
    {
        init();
        lightManger lightmanger = new lightManger();
        elevatorManager elevatormanager = new elevatorManager();
        one.start();
        two.start();
        three.start();
        four.start();
        five.start();
    }
}
