package org.yanning.gradle.vcs_lib.gui;

import javax.swing.*;

public class HelloWorldSwing {
    /**{
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {
        // 创建及设置窗口
        for (UIManager.LookAndFeelInfo name:UIManager.getInstalledLookAndFeels()){
            System.out.println(name.getName()+"       :"+name.getClassName());
        }
        try {
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[0].getClassName());
        } catch (Exception e) {
            e.printStackTrace();
            // 确保一个漂亮的外观风格
            JFrame.setDefaultLookAndFeelDecorated(true);
        }
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加 "Hello World" 标签
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        // 显示窗口
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
