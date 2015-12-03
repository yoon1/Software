package view;

import com.intellij.vcs.log.graph.impl.facade.GraphChanges;
import model.User;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class Painter extends JPanel {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final Color BackColor = Color.white;

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Graphics2D g2d;
    private String colorInfo;

    public ArrayList<Point> points = new ArrayList<Point>();

    private Color currentColor;
    String paintInfo;
    BufferedImage bImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    public Painter()
    {
        setBorder(BorderFactory.createLineBorder(Color.black));

        //Basic Settings for bImage
        Graphics g = bImage.getGraphics();
        g.setColor(BackColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(User.getUser().getIsHost()) {
                    points.clear();
                    points.add(e.getPoint());
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if(User.getUser().getIsHost()) {
                    points.add(e.getPoint());
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if(User.getUser().getIsHost()) {
                    points.add(e.getPoint());
                    repaint();
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH,HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            drawIntoBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(bImage, 0, 0, null);
    }

    public void drawIntoBufferedImage() throws IOException {
        Graphics g = bImage.getGraphics();
        freehandLines(g);
        g.dispose();
    }

    public void freehandLines(Graphics g) throws IOException {
        if(points != null && points.size() > 1)
        {

            for(int i = 0; i < points.size()-1;i++)
            {
                g2d = (Graphics2D) g;
                if(currentColor == Color.white)
                    //이거 굵기 설정하는거
                    g2d.setStroke(new BasicStroke(30));
                else//흰색(지우개)아니면 원래대로
                    g2d.setStroke(new BasicStroke(1));

                g.setColor(getCurrentColor());

                x1 = points.get(i).x;
                y1 = points.get(i).y;
                x2 = points.get(i+1).x;
                y2 = points.get(i+1).y;

                colorInfo = getCurrentColor().getRed() + "," + getCurrentColor().getGreen() + "," + getCurrentColor().getBlue();

                RoomBackground.getDos().writeUTF("/paintinfos/" + x1 + "," + y1 + "," + x2 + "," + y2 + "/" + colorInfo);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    //clear drawings method
    public void clearDrawings()
    {
        if(points!=null)
        {
            points.clear();
            Graphics g = bImage.getGraphics();
            g.setColor(BackColor);
            g.fillRect(0, 0, WIDTH, WIDTH);
            g.dispose();
            repaint();
        }
    }

    public void setCurrentColor(Color currentColor) {
        if(currentColor == null)
        {
            currentColor = Color.BLACK;
        }else{
            this.currentColor = currentColor;
        }
    }

    public Color getCurrentColor() {
        if (currentColor == null)
            return Color.BLACK;
        else
            return currentColor;
    }


}