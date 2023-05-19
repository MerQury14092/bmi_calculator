package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Exception{
        BufferedImage background = ImageIO.read(new URL("https://pngicon.ru/file/uploads/naruto.png")),
                icon = ImageIO.read(new URL("https://cdn-icons-png.flaticon.com/512/10008/10008838.png")),
                frTextArea = ImageIO.read(new URL("https://avatanplus.com/files/resources/mid/5777957e9982c155ab1ff6a5.png"));

        ImtResults results = new ImtResults();

        JFrame frame = new JFrame("Калькулятор ИМТ");
        frame.setIconImage(icon.getScaledInstance(32,32,Image.SCALE_SMOOTH));

        GridBagLayout layout = new GridBagLayout();
        layout.rowHeights = IntStream.generate(() -> 20).limit(20).toArray();
        layout.columnWidths = IntStream.generate(() -> 20).limit(20).toArray();
        frame.setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        JLabel bckgrnd = new JLabel();
        bckgrnd.setIcon(new ImageIcon(background.getScaledInstance(400,400,Image.SCALE_SMOOTH)));

        constraints.gridx = 8;
        constraints.gridy = 10;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;

        JTextArea mass = new JTextArea("      ");
        JLabel mass_bckgrnd = new JLabel();
        mass_bckgrnd.setIcon(new ImageIcon(frTextArea.getScaledInstance(60,20,Image.SCALE_SMOOTH)));
        frame.add(mass, constraints);
        frame.add(mass_bckgrnd, constraints);

        constraints.gridx = 8;
        constraints.gridy = 12;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;

        JTextArea height = new JTextArea("      ");
        JLabel height_bckgrnd = new JLabel();
        height_bckgrnd.setIcon(new ImageIcon(frTextArea.getScaledInstance(60,20,Image.SCALE_SMOOTH)));
        frame.add(height, constraints);
        frame.add(height_bckgrnd, constraints);

        constraints.gridx = 8;
        constraints.gridy = 9;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        JLabel mass_text = new JLabel("Введите массу");
        mass_text.setForeground(Color.WHITE);
        frame.add(mass_text, constraints);

        constraints.gridx = 8;
        constraints.gridy = 11;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        JLabel height_text = new JLabel("Введите рост");
        height_text.setForeground(Color.WHITE);
        frame.add(height_text, constraints);

        constraints.gridx = 8;
        constraints.gridy = 15;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        JButton calculate = new JButton("Расчитать");
        calculate.setForeground(Color.BLUE);
        frame.add(calculate, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 20;
        constraints.gridheight = 20;
        frame.add(bckgrnd, constraints);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        calculate.addActionListener((e) -> {
            if(isDigit(mass.getText()) && isDigit(height.getText()))
                results.show(Double.parseDouble(mass.getText())/(Math.pow(Double.parseDouble(height.getText())/100,2)));
        });

        frame.setVisible(true);

    }

    private static boolean isDigit(String str){
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static class ImtResults extends JFrame{
        BufferedImage cur_result, skinny, fatty, normal;
        private long current_time;

        public ImtResults() throws Exception {
            initImages();

            setUndecorated(true);
            getContentPane().setPreferredSize(new Dimension(200,200));
            pack();
            setLocationRelativeTo(null);
            WindowListener listener = new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {

                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    close();
                }
            };
            addWindowListener(listener);
        }

        public void show(double imt){
            if(imt < 18)
                cur_result = skinny;
            else if(imt > 25)
                cur_result = fatty;
            else
                cur_result = normal;
            setVisible(true);
        }

        private void close(){
            setVisible(false);
        }

        @Override
        public void paint(Graphics g) {
            g = getContentPane().getGraphics();
            g.drawImage(cur_result,0,0,200,200,null);
        }

        private void initImages() throws Exception{
            current_time = 0;
            cur_result = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
            fatty = ImageIO.read(new URL("https://avatars.mds.yandex.net/i?id=43f4ca0682a7cfe6dfeb6e2cd347d46ea9010b7d-7758455-images-thumbs&n=13"));
            skinny = ImageIO.read(new URL("https://memesmix.net/media/created/kem7sh.jpg"));
            normal = ImageIO.read(new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStgdzsS_RL2W7RJ0CVm_oUR_mXUUV0xGWTkg&usqp=CAU"));
        }
    }
}