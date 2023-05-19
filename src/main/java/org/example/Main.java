package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.stream.IntStream;


public class Main {
    public static void main(String[] args) throws Exception{
        // Подгрузка нужных картинок
        BufferedImage backgroundImage = ImageIO.read(new URL("https://pngicon.ru/file/uploads/naruto.png")),
                applicationIcon = ImageIO.read(new URL("https://cdn-icons-png.flaticon.com/512/10008/10008838.png")),
                textAreaFrame = ImageIO.read(new URL("https://avatanplus.com/files/resources/mid/5777957e9982c155ab1ff6a5.png"));

        // Заблаговременная инициализация окна вывода результата
        ImtResults resultWindow = new ImtResults();

        // Основное окно
        JFrame frame = new JFrame("Калькулятор ИМТ");
        frame.setIconImage(applicationIcon.getScaledInstance(32,32,Image.SCALE_SMOOTH));

        // Объявление и инициализация компоновщика Grid для нашего окна, а так же установка количества (20x20) ячеек по 20 пикселей
        GridBagLayout layout = new GridBagLayout();
        layout.rowHeights = IntStream.generate(() -> 20).limit(20).toArray();
        layout.columnWidths = IntStream.generate(() -> 20).limit(20).toArray();
        frame.setLayout(layout);

        // Объект, который будет помогать ставить новые компоненты на нужные места по сетке
        GridBagConstraints locator = new GridBagConstraints();

        // Объявление и инициализация фонового изображения
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(backgroundImage.getScaledInstance(400,400,Image.SCALE_SMOOTH)));

        // Объявление и инициализация текстового поля ввода веса а так же подгон локатора под нужные координаты
        locator.gridx = 8;
        locator.gridy = 10;
        locator.gridwidth = 3;
        locator.gridheight = 1;

        JTextArea mass = new JTextArea("      ");
        JLabel mass_background = new JLabel();
        mass_background.setIcon(new ImageIcon(textAreaFrame.getScaledInstance(60,20,Image.SCALE_SMOOTH)));
        frame.add(mass, locator);
        frame.add(mass_background, locator);

        // О. и И. текстового поля ввода роста
        locator.gridx = 8;
        locator.gridy = 12;
        locator.gridwidth = 3;
        locator.gridheight = 1;

        JTextArea height = new JTextArea("      ");
        JLabel height_background = new JLabel();
        height_background.setIcon(new ImageIcon(textAreaFrame.getScaledInstance(60,20,Image.SCALE_SMOOTH)));
        frame.add(height, locator);
        frame.add(height_background, locator);


        // О. и И. подсказки к полю ввода mass
        locator.gridx = 8;
        locator.gridy = 9;
        locator.gridwidth = 3;
        locator.gridheight = 1;
        JLabel mass_text = new JLabel("Введите массу");
        mass_text.setForeground(Color.WHITE);
        frame.add(mass_text, locator);

        // О. и И. подсказки к полю ввода height
        locator.gridx = 8;
        locator.gridy = 11;
        locator.gridwidth = 3;
        locator.gridheight = 1;
        JLabel height_text = new JLabel("Введите рост");
        height_text.setForeground(Color.WHITE);
        frame.add(height_text, locator);

        // // О. и И. кнопки расчитать
        locator.gridx = 8;
        locator.gridy = 15;
        locator.gridwidth = 3;
        locator.gridheight = 1;
        JButton calculate = new JButton("Расчитать");
        calculate.setForeground(Color.BLUE);
        frame.add(calculate, locator);

        // Установка фонового изображения
        locator.gridx = 0;
        locator.gridy = 0;
        locator.gridwidth = 20;
        locator.gridheight = 20;
        frame.add(background, locator);

        // Стандартные установки настроек для JFrame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        // Добавление функции, которая при нажатии на calculate вызывает метод show у объекта resultWindow и передает туда расчитанные данные
        calculate.addActionListener((e) -> {
            if(isDigit(mass.getText()) && isDigit(height.getText()))
                resultWindow.show(Double.parseDouble(mass.getText())/(Math.pow(Double.parseDouble(height.getText())/100,2)));
        });

        // Показываем окно
        frame.setVisible(true);

    }

    // Стандартный предикат проверки строки на число
    private static boolean isDigit(String str){
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static class ImtResults extends JFrame{
        // Нужные ресурсы, cur_result является temporary решением, остальные подгружаются один раз
        BufferedImage cur_result, skinny, fatty, normal;

        public ImtResults() throws Exception {
            // Подгрузка ресурсов
            loadAssets();

            // Стандартные операции над JFrame
            setUndecorated(true);
            getContentPane().setPreferredSize(new Dimension(200,200));
            pack();
            setLocationRelativeTo(null);

            // Слушатель, который при деактивации окна, вызывает close()
            WindowListener listener = new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {}
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
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
            // Закрывает окно
            setVisible(false);
        }

        @Override
        public void paint(Graphics g) {
            // Отрисовывает картинку
            g = getContentPane().getGraphics();
            g.drawImage(cur_result,0,0,200,200,null);
        }

        private void loadAssets() throws Exception{
            // Подгружает картинки с заранее подготовленных URL
            cur_result = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
            fatty = ImageIO.read(new URL("https://avatars.mds.yandex.net/i?id=43f4ca0682a7cfe6dfeb6e2cd347d46ea9010b7d-7758455-images-thumbs&n=13"));
            skinny = ImageIO.read(new URL("https://memesmix.net/media/created/kem7sh.jpg"));
            normal = ImageIO.read(new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStgdzsS_RL2W7RJ0CVm_oUR_mXUUV0xGWTkg&usqp=CAU"));
        }
    }
}