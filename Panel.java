import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel implements ActionListener {

	boolean start = false;
	ArrayList<Point> points = new ArrayList<Point>();
	int panel_x = 450, panel_y = 25, przesuniecie = 250;
	int lower_bound = -250, higher_bound = 250;

	double x = 1;// random number

	int nb_of_agents = 40;
	double C = (0.5 + 0.8) / 2;
	double F = (0.1 + 0.9) / 2;

	JTextField text_agents = new JTextField(String.valueOf(nb_of_agents));
	JTextField text_C = new JTextField(String.valueOf(C));
	JTextField text_F = new JTextField(String.valueOf(F));

	public Panel() {

		int window_width = 1000;
		int window_height = 600;
		setPreferredSize(new Dimension(window_width, window_height));
		this.setLayout(null);

		JButton startButton = new JButton("START");
		JLabel label_agents = new JLabel("Number of agents");
		JLabel label_C = new JLabel("C");
		JLabel label_F = new JLabel("F");

		startButton.setBounds(100, window_height - 100, 100, 30);
		label_agents.setBounds(100, window_height - 200, 200, 30);
		label_F.setBounds(100, window_height - 260, 200, 30);
		label_C.setBounds(100, window_height - 320, 200, 30);

		text_F.setBounds(250, window_height - 320, 100, 30);
		text_C.setBounds(250, window_height - 260, 100, 30);
		text_agents.setBounds(250, window_height - 200, 100, 30);

		startButton.addActionListener(this);

		this.add(startButton);
		this.add(label_agents);
		this.add(label_C);
		this.add(label_F);
		this.add(text_C);
		this.add(text_F);
		this.add(text_agents);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		F = Double.parseDouble(text_F.getText());
		C = Double.parseDouble(text_C.getText());
		nb_of_agents = Integer.parseInt(text_agents.getText());

		for (int i = 0; i < nb_of_agents; i++) {
			GenRandPoint();
		}

		start = !start;

		if (!start)
			points.clear();
	}

	public void GenRandPoint() {
		points.add(new Point((int) GenRandNumb(), (int) GenRandNumb()));
	}

	public double GenRandNumb() {
		return GenRandNumb(lower_bound, higher_bound);
	}

	public double GenRandNumb(int l, int h) {
		long m = 2147483647, a = 16808, c = 0;

		x = (a * x + c) % m;
		return (l + (x / m) * (h - l));
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawRect(panel_x, panel_y, 500, 500);
		g2d.setColor(Color.RED);

		if (start) {
			for (Point point : points) {
				g2d.fillOval(panel_x + point.x + przesuniecie, panel_y + point.y + przesuniecie, 4, 4);
			}

			for (Point point : points) {
				Point a, b, c;

				do {
					a = points.get((int) GenRandNumb(0, nb_of_agents));
				} while (a == point);
				do {
					b = points.get((int) GenRandNumb(0, nb_of_agents));
				} while (b == point || b == a);
				do {
					c = points.get((int) GenRandNumb(0, nb_of_agents));
				} while (c == point || c == b || c == a);

				//////////////////////////////
				int x = (int) (c.getX() + F * (b.getX() - a.getX()));
				int y = (int) (c.getY() + F * (b.getY() - a.getY()));
				//////////////////////////////

				if (!(GenRandNumb(0, 1) > C))
					if (x >= lower_bound && x <= higher_bound)
						if (y >= lower_bound && y <= higher_bound)
							if (function(new Point(x, y)) < function(point)) {
								point.x = x;
								point.y = y;
							}
			}
		}

		repaint();
		try {
			TimeUnit.MILLISECONDS.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public double function(Point var) {
		return Example2(var);
	}

	public double Example(Point var) {
		return Math.sin(var.getX()) + Math.pow(var.getX(), 2) - Math.pow(var.getY(), 2);// sin(x)+x^2-y^2
	}

	public double Example2(Point var) {
		return Math.pow(var.getX(), 2) + Math.pow(var.getY(), 2)
				+ 25 * (Math.pow(Math.sin(var.getX()), 2) + Math.pow(Math.sin(var.getY()), 2));
	}
	/*
	 * public double Auckley(Point var) { double sum1 = 0.0; double sum2 = 0.0;
	 * 
	 * for (int i = 0 ; i < x.length ; i ++) { sum1 += Math.pow(x[i], 2); sum2 +=
	 * (Math.cos(2*Math.PI*x[i])); }
	 * 
	 * return -20.0*Math.exp(-0.2*Math.sqrt(sum1 / ((double )x.length))) + 20 -
	 * Math.exp(sum2 /((double )x.length)) + Math.exp(1.0); }
	 */
}
