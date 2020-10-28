/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frog;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Admin
 */
public class Control implements KeyListener {

    private Farme_Frog f;
    private int heightPanel, widthPanel;
    private JButton button1 = new JButton();
    private JButton button2 = new JButton();
    private JButton button3 = new JButton();
    private JButton button4 = new JButton();
    private JButton buttonFrog = new JButton();
    private Timer timer;
    private int point;
    private File file = new File("save.txt");
    private int distance, distance1, distance2;
    int x1;
    int h1;
    int h2;
    int x2;
    int h3;
    int h4;
    int yFrog;
    private int countTemp = 0;

    public Control(Farme_Frog f, int heightPanel, int widthPanel) {
        this.f = f;
        this.heightPanel = heightPanel;
        this.widthPanel = widthPanel;
        f.getButtonPause().addKeyListener(this);
        f.getButtonSave().addKeyListener(this);
       if (file.exists()) {
           loadFile();
       } else {
           x1 = 150;
           yFrog = 70;
           distance = 150;
           distance1 = 175;
           distance2 = 200;
           h1 = (heightPanel - distance) * 3 / 5;
           h2 = (heightPanel - distance1) * 2 / 5;
           x2 = x1 + 35 * 2 + distance2;
           h3 = (heightPanel - distance) - h1;
           h4 = (heightPanel - distance1) - h2;
           point = 0;
           f.getDisplayPoint().setText("Point : " + point);
       }

    }

    public void addButton() {
        button1.setBounds(x1, 0, 35, h1);
        button2.setBounds(x2, 0, 35, h2);
        button3.setBounds(x1, heightPanel - h3, 35, h3);
        button4.setBounds(x2, heightPanel - h4, 35, h4);
        button1.setBackground(Color.GRAY);
        button2.setBackground(Color.GRAY);
        button3.setBackground(Color.GRAY);
        button4.setBackground(Color.GRAY);
        f.getPanelDisplay().add(button1);
        f.getPanelDisplay().add(button2);
        f.getPanelDisplay().add(button3);
        f.getPanelDisplay().add(button4);
        buttonFrog.setBounds(30, yFrog, 35, 35);
        buttonFrog.setIcon(new ImageIcon("C:\\Users\\Admin\\Pictures\\Saved Pictures\\admin1.png"));
        f.getPanelDisplay().add(buttonFrog);
    }

    private boolean checkChangeStatus = false, checkMove = true;
    int count = 0, count2 = 0, temp;
    private boolean markChangePoint = true;

    public void run() {
        timer = new Timer(15, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (press) {
                    yFrog = yFrog - 20;
                    press = false;
                }

//                checkChangeStatus = changeStatus(point);
//                if (checkChangeStatus) {
//                    if (buttonFrog.getX() == x1 || buttonFrog.getX() == x2) {
//                        int tempValue = changeDistance(point);
//                        distance -= tempValue;
//                        distance1 -= tempValue;
//                        distance2 -= changeDistance2(point);
//                        checkChangeStatus = false;
//                        checkMove = true;
//                        System.out.println(distance2);
//                    }
//                }

                addButton();
                int changeSpeed = changeSpeed(point);
                x1 -= changeSpeed;
                x2 -= changeSpeed;
             //   yFrog++;
                if (checkMove == true && x2 <= -35) {
                    x2 = x1 + 35 * 2 + distance2 - 85;
                    checkMove = false;
                }
              
                if (x1 <= -35) {
                    markChangePoint = true;
                    countTemp = 0;
                    x1 = widthPanel;
                    Random random = new Random();

                    h1 = random.nextInt(50) + (heightPanel - distance - 35) * 3 / 5;
                    h3 = random.nextInt(20) + heightPanel - h1 - distance;
                }
                if (x2 <= -35) {
                    markChangePoint = true;
                    countTemp = 0;
                    x2 = widthPanel;
                    Random random = new Random();

                    h2 = random.nextInt(50) + (heightPanel - distance1 - 35) * 3 / 5;
                    h4 = random.nextInt(20) + heightPanel - h2 - distance1;
                }
//                if (!checkBlock()) {
//                    timer.stop();
//                    newGame();
//                }

//                if (buttonFrog.getX() == button1.getX() || buttonFrog.getX() == button2.getX()) {
//                    point++;
//                    f.getDisplayPoint().setText(String.valueOf("Point : " + point));
//                }
                if (changePoint(buttonFrog, button1, button2)) {
                    countTemp += changeSpeed;
                    if (countTemp == 30 && markChangePoint) {
                        point++;
                        f.getDisplayPoint().setText(String.valueOf("Point : " + point));
                        markChangePoint = false;
                    }
                }

            }
        });
        timer.start();

    }

    public boolean changePoint(JButton button1, JButton button2, JButton button3) {
        Rectangle bt1 = new Rectangle(button1.getX(), button1.getY(), button1.getWidth(), button1.getHeight());
        Rectangle bt2 = new Rectangle(button2.getX(), button2.getY(), button2.getWidth(), heightPanel);
        Rectangle bt3 = new Rectangle(button3.getX(), button3.getY(), button3.getWidth(), heightPanel);
        if (bt1.intersects(bt3) || bt1.intersects(bt2)) {
            return true;
        }
        return false;
    }

    public boolean checkBlock() {
        if (buttonFrog.getY() <= 0 || buttonFrog.getY() >= heightPanel - 40) { // buttonForg.getY() la toa do Y cua Frog 
            return false;
        }
        Rectangle btf = new Rectangle(buttonFrog.getX(), buttonFrog.getY(), buttonFrog.getWidth(), buttonFrog.getHeight());
        Rectangle bt1 = new Rectangle(button1.getX(), button1.getY(), button1.getWidth(), button1.getHeight());
        Rectangle bt2 = new Rectangle(button2.getX(), button2.getY(), button2.getWidth(), button2.getHeight());
        Rectangle bt3 = new Rectangle(button3.getX(), button3.getY(), button3.getWidth(), button3.getHeight());
        Rectangle bt4 = new Rectangle(button4.getX(), button4.getY(), button4.getWidth(), button4.getHeight());
        if (btf.intersects(bt1) || btf.intersects(bt2) || btf.intersects(bt3) || btf.intersects(bt4)) {
            return false;
        }
        return true;
    }

    public void newGame() {

        if (!file.exists()) {
            int value = JOptionPane.showConfirmDialog(null, "Do you want to make new game ?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (value == JOptionPane.YES_OPTION) {
                x1 = 150;
                yFrog = 70;
                distance = 150;
                distance1 = 130;
                distance2 = 210;
                h1 = (heightPanel - distance) * 3 / 5;
                h2 = (heightPanel - distance1) * 2 / 5;
                x2 = x1 + 35 * 2 + distance2;
                h3 = (heightPanel - distance) - h1;
                h4 = (heightPanel - distance1) - h2;
                point = 0;
                f.getDisplayPoint().setText("Point : " + point);
                timer.restart();
            } else {
                System.exit(0);
            }
        } else {
            Object mess[] = {"New Game", "Continue", "Exit"};
            int temp = JOptionPane.showOptionDialog(null, "What is your choice ?", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, mess, mess[0]);
            if (temp == 0) {
                x1 = 150;
                yFrog = 70;
                distance = 150;
                distance1 = 130;
                distance2 = 210;
                h1 = (heightPanel - distance) * 2 / 5;
                h2 = (heightPanel - distance1) * 3 / 5;
                x2 = x1 + 35 * 2 + distance2;
                h3 = (heightPanel - distance) - h1;
                h4 = (heightPanel - distance1) - h2;
                point = 0;
                f.getDisplayPoint().setText("Point : " + point);
                timer.restart();
            } else if (temp == 1) {
                loadFile();
                timer.restart();
            } else {
                System.exit(0);
            }
        }
    }

    public void pause() {
        timer.stop();
        System.out.println(count);
        int option = JOptionPane.showConfirmDialog(null, "Do you want to continue or exit ?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == 0) {
            timer.restart();
        } else {
            int mark = getIntFromString(f.getDisplayPoint().getText());
            JOptionPane.showMessageDialog(f, "Thanks for playing !\nYour mark is " + mark);
            System.exit(0);
        }
    }

    public void save() {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String string = x1 + " | " + x2 + " | " + h1 + " | " + h2 + " | " + h3 + " | " + h4 + " | " + yFrog + " | " + point + "|" + distance + "|" + distance1 + "|" + distance2;
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
        }
    }

    private boolean press = false, release = true;

    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == ke.VK_UP) {
            if (release) {
                press = true;
                release = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        release = true;
    }

    public int getIntFromString(String string) {
        String stringTemp = "";
        for (int i = 0; i < string.length(); i++) {
            if (!(string.charAt(i) >= '0' && string.charAt(i) <= '9')) {
                continue;
            }
            stringTemp += string.charAt(i);
        }
        return Integer.parseInt(stringTemp);
    }

    public void loadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String string = "";
            int c = fileInputStream.read();
            while (c != -1) {
                string += (char) c;
                c = fileInputStream.read();
            }
            fileInputStream.close();
            String[] string2 = string.split("\\|");
            x1 = Integer.parseInt(string2[0].trim());
            h1 = Integer.parseInt(string2[2].trim());
            h2 = Integer.parseInt(string2[3].trim());
            x2 = Integer.parseInt(string2[1].trim());
            h3 = Integer.parseInt(string2[4].trim());
            h4 = Integer.parseInt(string2[5].trim());
            yFrog = Integer.parseInt(string2[6].trim());
            point = Integer.parseInt(string2[7].trim());
            distance = Integer.parseInt(string2[8].trim());
            distance1 = Integer.parseInt(string2[9].trim());
            distance2 = Integer.parseInt(string2[10].trim());

            f.getDisplayPoint().setText("Point : " + point);

        } catch (IOException | NumberFormatException e) {
        }
    }

    public int changeSpeed(int point1) {

        if (point1 >= 0 && point1 <= 5) {
            return 1;
        } else if (point1 > 5 && point1 <= 10) {
            return 3;
        } else if (point1 > 10 && point1 <= 20) {
            return 5;
        } else {
            return 8;
        }
    }

    public int changeDistance(int point1) {
        if (point1 >= 0 && point1 <= 5) {
            return 15;
        } else if (point1 > 5 && point1 <= 10) {
            return 25;
        } else if (point1 > 10 && point1 <= 20) {
            return 35;
        } else {
            return 0;
        }
    }

    public int changeDistance2(int point1) {
        if (point1 >= 0 && point1 <= 5) {
            return 20;
        } else if (point1 > 5 && point1 <= 10) {
            return 25;
        } else if (point1 > 10 && point1 <= 20) {
            return 35;
        } else {
            return 0;
        }
    }

    public boolean changeStatus(int point) {
        if (point == 5 || point == 10 || point == 20 || point == 40) {
            return true;
        }
        return false;
    }

}
