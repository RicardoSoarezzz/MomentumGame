
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MomentumResponse implements Runnable {
    private Thread t;
    private Socket s;
    private int port = 47808;
    private static final String SERVER_IP = "127.0.0.1";
    private boolean run;
    private JFrame jf = new JFrame("UPT - IA - MomentumAB");
    private JPanel jMainPanel;
    private JTextField jt;
    private JTextField jplayer;
    private JTextField jc;
    private BufferedReader in = null;
    private BufferedWriter out = null;

    public MomentumResponse() {
        jf = new JFrame("UPT - IA - MomentumAB");
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int res = confirmExit();
                if (res == JOptionPane.YES_OPTION) { // confirmed ?
                    System.exit( 0);
                }
            }
        };
        this.jMainPanel = new JPanel();
        this.jMainPanel.setLayout(new BorderLayout());
        this.jf.add(this.jMainPanel);
        JPanel jnorth = new JPanel();
        this.jMainPanel.add(jnorth, "North");
        this.jt = new JTextField("    ", 24);
        jnorth.add(this.jt);
        jnorth.add(new JLabel("Counting: "));
        this.jc = new JTextField("      ", 24);
        jnorth.add(this.jc);
        jnorth.add(new JLabel("Player: "));
        this.jplayer = new JTextField("  ", 3);
        jnorth.add(this.jplayer);
        JPanel jsouth = new JPanel();
        this.jMainPanel.add(jsouth, "South");
        JButton jok = new JButton("  OK  ");
        jsouth.add(jok);
        JButton jend = new JButton("  Exit  ");
        jsouth.add(jend, "South");
        jend.addActionListener( new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
                int res = confirmExit();
                if (res == JOptionPane.YES_OPTION) { // confirmado ?
                    System.exit( 0);
                }
            }
        });
        this.run = false;
        this.jf.pack();
        this.jf.setVisible(true);
    }

    public void begin() {
        this.run = true;
        this.jt.setEnabled(false);
        this.t = new Thread(this);
        this.t.start();
    }

    public void run() {
        try {
            this.s = new Socket("127.0.0.1", this.port);
        } catch (Exception var9) {
            var9.printStackTrace();
            System.out.println("Error opening socket");
            System.exit(0);
        }

        try {
            this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream()));
        } catch (Exception var8) {
            var8.printStackTrace();
            System.out.println("Error opening socket");
            System.exit(0);
        }

        System.out.println("Start run");

        while(this.run) {
            String st = null;

            try {
                if (this.in.ready()) {
                    this.jt.setText("");
                    this.jc.setText("");
                    st = this.in.readLine();
                    System.out.println("readLine " + st);
                    if (st.length() < 4) {
                        this.jplayer.setText(st);
                        NodeGameAB.setPlayer(st);
                        continue;
                    }

                    this.jt.setBackground(Color.yellow);
                    this.jt.setEnabled(true);
                    GameMomentumAB initial = new GameMomentumAB(st);
                    String res = initial.processAB(this.jc);
                    this.jt.setText(res);
                    System.out.println(initial.toString());
                    String str = this.jt.getText().trim();  // Remove extra space at the start and end

                    try {
                        this.out.write(str + "\n");
                        this.out.flush();
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        System.exit(0);
                    }


                    System.out.println("Sent move: " + str);
                    this.jt.setBackground(Color.white);
                    this.jt.setEnabled(false);
                }
            } catch (Exception var10) {
                var10.printStackTrace();
                System.exit(0);
            }

            try {
                Thread.yield();
            } catch (Exception var6) {
            }
        }

    }

    public void stop() {
        this.run = false;
    }

    protected int confirmExit() {
        return JOptionPane.showConfirmDialog((Component)null, " Confirm end of program ? ", " UPT - IA - Momentum ", 2, 2);
    }

    public static void main(String[] args) {
        MomentumResponse r = new MomentumResponse();
        r.begin();
    }
}
