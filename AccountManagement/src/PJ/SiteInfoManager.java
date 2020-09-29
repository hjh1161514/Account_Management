package PJ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SiteInfoManager extends JFrame {
	// ���ĺз� ��������
	// ���̺������ ���̴� data������ arraylist�� �ٲ� ��.
	public String[] SearchSelected = { "�Ϲ�", "����", "����", "�б�" }; // �˻����� ��������
	private String[] SortCategorize_mini = { "����Ʈ �̸�", "����Ʈ �ּ�", "��ȣ��" }; // ���� ����Ʈ �������� // SortCategorize2�� ����
	private String[] SortCtegorize1 = { "��ü", "�Ϲ�", "����", "����", "�б�" };
	private JTextField SearchFilter = new JTextField(8);

	private JComboBox<String> CategorizeCombo = new JComboBox<String>(SearchSelected);
	private JComboBox<String> SearchCombo = new JComboBox<String>(SortCtegorize1);
	private JComboBox<String> SiteCombo = new JComboBox<String>(SortCategorize_mini);
	String[] categoryMenu = { "�Ϲ�", "����", "����", "�б�" };
	String[] preferMenu = { "�١١١١�", "�١١١�", "�١١�", "�١�", "��" };
	JComboBox<String> categoryCombo = new JComboBox<String>(categoryMenu);
	JComboBox<String> preferCombo = new JComboBox<String>(preferMenu);

	SiteInfoList sil;
	SiteDetailInfo sdi;
	SiteCategory sic;
	SiteDetailInfo user;
	SiteCategoryList scg;
	
	private InfoTableModel infotableModel;
	private JTable table;
	private int number = 0;
	JMenuBar mb = new JMenuBar();
	JLabel label = new JLabel(number + "���� ����Ʈ��  ��ϵǾ��ֽ��ϴ�.");
	
	private LoginDialog logindialog;

	private JButton SortButton = new JButton("����");
	private JButton SortDefault = new JButton("�⺻");

	JTextArea textArea = new JTextArea(6, 16);
	JTextField name = new JTextField("", 10);
	JTextField url = new JTextField("", 8);
	JTextField id = new JTextField("", 6);
	JTextField passwd = new JTextField("", 6);
	private JButton rewriteButton = new JButton("���� �ۼ�(N)");
	private JButton inputButton = new JButton("�Է�(I)");
	JMenuItem item8 = new JCheckBoxMenuItem("�ڵ� �α���(L)");
	JMenuItem item9 = new JCheckBoxMenuItem("�������� ���� ���� ����ϱ�(V)");
	JButton delete = new JButton("����");
	
	int index = 4;
	int tablerow;
	
	JPanel panel;
	JPanel panel2;
	JTextField idfield;
	JTextField pwfield;
	JCheckBox checkbox;
	
	SiteDetailInfo[] vvv = new SiteDetailInfo[100];

	Vector<SiteDetailInfo> vv = new Vector<SiteDetailInfo>();
	Vector<SiteDetailInfo> s = new Vector<SiteDetailInfo>();
	Vector<SiteDetailInfo> f = new Vector<SiteDetailInfo>();
	Vector<SiteDetailInfo> fa = new Vector<SiteDetailInfo>();
	
	public SiteInfoManager() {

		super("�� ��������");
		createMenu();

		setSize(850, 600);
		setLocation(500, 200);
		add(completePanel());
		setVisible(true);

		logindialog = new LoginDialog(this, "��й�ȣ �α���");

		Point p = SiteInfoManager.this.getLocationOnScreen();
		int cx = SiteInfoManager.this.getWidth() / 2;
		int cy = SiteInfoManager.this.getHeight() / 2;
		logindialog.setLocation(p.x + cx - logindialog.getWidth() / 2, p.y + cy - logindialog.getHeight() / 2);

		BufferedReader bfr;
		try {
			// üũ�ڽ�
			bfr = new BufferedReader(new FileReader("logincheck.txt"));
			String line = ""; // �ƹ� �͵� ����
			while ((line = bfr.readLine()) != null) {
				if (line.equals("true")) {
					logindialog.setVisible(false);
					item8.setSelected(true);
				} else {
					logindialog.setVisible(true);
				}
			}
			bfr.close();
		} catch (FileNotFoundException e) {
			System.out.println("������ ã�� �� �����ϴ�");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader bfr2;
		try {
			// üũ�ڽ�
			bfr2 = new BufferedReader(new FileReader("idcheckbox.txt"));
			String line = ""; // �ƹ� �͵� ����
			while ((line = bfr2.readLine()) != null) {
				if (line.equals("true_true")) { // ��������������� üũ, ������������ üũ
					checkbox.setSelected(true); // ������������ üũ
					item9.setSelected(true); // ��������������� üũ
				} else if (line.equals("true")) { // ��������������� üũ, ������������ ��üũ
					item9.setSelected(true); // ��������������� üũ
				}
			}
			bfr2.close();
		} catch (FileNotFoundException e) {
			System.out.println("������ ã�� �� �����ϴ�");
		} catch (IOException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Close();
			}
		});
	}

	public void Close() { // ���� ó��
		int exitOption = JOptionPane.showConfirmDialog(SiteInfoManager.this, "���� �����Ͻðڽ��ϱ�?", "���� Ȯ��",
				JOptionPane.YES_NO_OPTION);

		if (exitOption == JOptionPane.YES_OPTION) {
			System.exit(JFrame.EXIT_ON_CLOSE); // �������� ����
		} else if ((exitOption == JOptionPane.NO_OPTION) || (exitOption == JOptionPane.CLOSED_OPTION)) {
			return; // �ƹ� �۾� ���� ���̾�α� ���ڸ� �ݴ´�
		}
	}

	private void createMenu() {
		// ����: 3��
		// ����: �޴� ������ �°� ����
		JMenu fileMenu = new JMenu("����(F)");
		JMenu manageMenu = new JMenu("����(M)");
		JMenu settingMenu = new JMenu("����(S)");
		JMenu HelpMenu = new JMenu("����(H)");

		JMenu subMenu = new JMenu("�׸�(T)");
		settingMenu.add(subMenu);
		JMenuItem subitem1 = new JMenuItem("�׸�����");

		subMenu.add(subitem1);

		subitem1.addActionListener(listenerThema);
		subitem1.addActionListener(listenerThema);

		fileMenu.setMnemonic('F');
		manageMenu.setMnemonic('M');
		settingMenu.setMnemonic('S');
		subMenu.setMnemonic('T');
		HelpMenu.setMnemonic('H');
		mb.add(fileMenu);
		mb.add(manageMenu);
		mb.add(settingMenu);
		mb.add(HelpMenu);
		JMenuItem item10 = new JMenuItem("����(U)");
		HelpMenu.add(item10);
		item10.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 JOptionPane.showMessageDialog(null, "1. ���̺� ��� �κ��� ���ڸ� ������ �ڵ����� �˴ϴ�.\n2. "
				 		+ "�Է� �� ����Ʈ�̸��� ����Ʈ �ּҴ� �ݵ�� �����ּ���\n3. �����Ȳ�����÷��� �ݵ�� �׷��Ⱥ����ư�� �����ּ���.", "��� ����",
						JOptionPane.INFORMATION_MESSAGE);
			}

		});

		JMenuItem item1 = new JMenuItem("���� ���Ͽ��� ��������(I)");
		item1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				// ���̺� �����ϱ�
				TableModel model = table.getModel();
				((InfoTableModel) model).removeAllRow();
				
				Workbook workbook = null;
				Sheet sheet = null;

				String excelFile = "excel2.xls";
				try {
					// ���������� �ν�
					workbook = Workbook.getWorkbook(new File(excelFile));

					// �������Ͽ� ���Ե� sheet�� �迭�� �����Ѵ�.
					// workbook.getSheets();

					if (workbook != null) {
						// �������Ͽ��� ù��° Sheet�� �ν�
						sheet = workbook.getSheet(0);

						if (sheet != null) {
							// ���ν� Cell a1 = sheet.getCell( �÷� Index, �� Index);
							// �� ���� String stringa1 = a1.getContents();

							// ��Ϲ�ö�� ��� ���� �����Ͱ� ���۵Ǵ� Row����
							int nRowStartIndex = 0;
							// ��Ϲ�ö�� ��� ���� �����Ͱ� �� Row����
							int nRowEndIndex = sheet.getColumn(2).length - 1;

							// ��Ϲ�ö�� ��� ���� �����Ͱ� ���۵Ǵ� Column����
							int nColumnStartIndex = 0;
							// ��Ϲ�ö�� ��� ���� �����Ͱ� ������ Column����
							int nColumnEndIndex = sheet.getRow(2).length - 1;

							String value[] = new String[100];

							int i = 0;

							for (int nRow = nRowStartIndex + 1; nRow <= nRowEndIndex; nRow++) {
								for (int nColumn = nColumnStartIndex; nColumn <= nColumnEndIndex; nColumn++) {
									value[i] = sheet.getCell(nColumn, nRow).getContents();
									if ((i % 4) == 3) {
										sdi = new SiteDetailInfo(value[i - 1], value[i], null, null, value[i - 3],
												value[i - 2], null);

										sil = new SiteInfoList(sdi);
										sil.addinfos(sdi);
										vv.add(sdi);
										// ����: siteDetailInfo ���Ϳ� ��ü�� �����ϰ� �ٽ� ���̺� ���� �����Ѵ�.
									}
									i++;

								}
							}
							table.setModel(new InfoTableModel(vv));
							table.updateUI();
						}

						else {
							System.out.println("Sheet�� ������ϴ�.!");
						}
					} else {
						System.out.println("WorkBook�� ������ϴ�!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (workbook != null) {
						workbook.close();
					}
				}
			}

		});
		item1.setMnemonic('I');
		JMenuItem item2 = new JMenuItem("���� ���Ϸ� ��������(E)...");
		item2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				WritableWorkbook workbook = null;
				WritableSheet sheet = null;
				File excelFile = new File("excel2.xls");
				jxl.write.Label num = null;

				int a = table.getRowCount();
				int b = table.getColumnCount();
				try {
					workbook = Workbook.createWorkbook(excelFile);
					workbook.createSheet("sheet1", 0);
					sheet = workbook.getSheet(0);

					for (int i = 0; i < infotableModel.getColumnCount(); i++) {
						Label column = new Label(i, 0, infotableModel.getColumnName(i));
						sheet.addCell(column);
					}

					for (int i = 0; i < a; i++) {
						for (int j = 0; j < b; j++) {
							num = new jxl.write.Label(j, i + 1, (String) infotableModel.getValueAt(i, j));
							sheet.addCell(num);
						}
					}
					workbook.write();
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		item2.setMnemonic('E');
		JMenuItem item3 = new JMenuItem("����(S)");
		item3.setMnemonic('S');
		fileMenu.add(item1);
		fileMenu.add(item2);
		fileMenu.add(item3);
		item3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OutputStream output;
				try {
					output = new FileOutputStream("user.txt");

					for (int i = 0; i < vv.size(); i++) {

						String id = "ID: " + vv.get(i).geti() + '\t';
						byte[] by = id.getBytes();

						String pw = "PW: " + vv.get(i).getp() + '\t';
						byte[] by1 = pw.getBytes();

						String name = "NAME: " + vv.get(i).getname() + '\t';
						byte[] by2 = name.getBytes();

						String url = "URL: " + vv.get(i).getu() + '\t';
						byte[] by3 = url.getBytes();

						String category = "Category: " + vv.get(i).getCategory() + '\t';
						byte[] by4 = category.getBytes();

						String prefer = "Prefer: " + vv.get(i).getPrefer() + '\t';
						byte[] by5 = prefer.getBytes();

						String memo = "MEMO: " + vv.get(i).getm() + '\t';
						byte[] by6 = memo.getBytes();

						output.write(by);
						output.write(by1);
						output.write(by2);
						output.write(by3);
						output.write(by4);
						output.write(by5);
						output.write(by6);

						output.write('\n');
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
				}

			}

		});
		fileMenu.addSeparator();

		JMenuItem item4 = new JMenuItem("�α׾ƿ�(O)");
		item4.setMnemonic('O');
		fileMenu.add(item4);
		item4.addActionListener(listener4);

		JMenuItem item5 = new JMenuItem("����(X)...");
		item5.addActionListener(listener5);
		item5.setMnemonic('X');
		fileMenu.add(item5);

		JMenuItem item6 = new JMenuItem("�����(U)");
		item6.setMnemonic('U');
		item6.addActionListener(listener8);

		JMenuItem item7 = new JMenuItem("����Ʈ �з�(C)");
		item7.setMnemonic('C');
		item7.addActionListener(listener7);

		manageMenu.add(item6);
		manageMenu.add(item7);

		item8.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				BufferedWriter bfw;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					try {
						// üũ�ڽ�
						bfw = new BufferedWriter(new FileWriter("logincheck.txt", false)); // �����
						bfw.write("true"); // üũ�Ǿ� ������ true ��ȯ
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("������ ã�� �� �����ϴ�");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else
					try {
						// üũ�ڽ�
						bfw = new BufferedWriter(new FileWriter("logincheck.txt", false)); // �����
						bfw.write("false"); // üũ ������ false ��ȯ
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("������ ã�� �� �����ϴ�");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		});
		item8.setMnemonic('L');

		item9.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				BufferedWriter bfw;
				if (e.getStateChange() == ItemEvent.SELECTED) { // ��������������°� ���õǾ�������
					try {
						// üũ�ڽ�
						bfw = new BufferedWriter(new FileWriter("idcheckbox.txt", false)); // �����
						bfw.write("true");
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("������ ã�� �� �����ϴ�");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else // �������� ������� �̼���
					try {
						// üũ�ڽ�
						bfw = new BufferedWriter(new FileWriter("idcheckbox.txt", false)); // �����
						bfw.write("false"); // üũ ������ false ��ȯ
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("������ ã�� �� �����ϴ�");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}

		});
		item9.setMnemonic('V');
		settingMenu.add(item8);
		settingMenu.add(item9);

		setJMenuBar(mb); // MB : �޴���

	}

	private ActionListener listenerThema = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String themaName = e.getActionCommand();
			if (themaName.equals("�׸�����")) {
				JFrame.setDefaultLookAndFeelDecorated(true);
				// ����:Look & Feel�� ����Ѵٴ� ��
				try {
					// ����:JFrame.setDefaultLookAndFeelDecorated(true);
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) { // nimbus �׸�
							UIManager.setLookAndFeel(info.getClassName());

							break;
						}
					}
					// ����:����ó��
				} catch (ClassNotFoundException ex) {
					java.util.logging.Logger.getLogger(SiteInfoManager.class.getName())
							.log(java.util.logging.Level.SEVERE, null, ex);
				} catch (InstantiationException ex) {
					java.util.logging.Logger.getLogger(SiteInfoManager.class.getName())
							.log(java.util.logging.Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(SiteInfoManager.class.getName())
							.log(java.util.logging.Level.SEVERE, null, ex);
				} catch (javax.swing.UnsupportedLookAndFeelException ex) {
					java.util.logging.Logger.getLogger(SiteInfoManager.class.getName())
							.log(java.util.logging.Level.SEVERE, null, ex);
				}
			}

		}

	};

	private ActionListener listener4 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			logindialog = new LoginDialog(SiteInfoManager.this, "����� �α���");

			Point p = SiteInfoManager.this.getLocationOnScreen();
			int cx = SiteInfoManager.this.getWidth() / 2;
			int cy = SiteInfoManager.this.getHeight() / 2;
			logindialog.setLocation(p.x + cx - logindialog.getWidth() / 2, p.y + cy - logindialog.getHeight() / 2);
			logindialog.setVisible(true);

		}

	};

	private ActionListener listener5 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(null, "���� �����Ͻðڽ��ϱ�?", "���� Ȯ��", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				System.exit(0);
			}
		}
	};

// url�ߺ� JOptionPane

	private ActionListener listener3 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (inputButton.getText().equals("�Է�(I)")) {
				// ����: tf���� false�� �ʱ�ȭ
				boolean tf = false;

				// ����: ó�� ���̺��� ���� ��, getRowCount>0�̻��϶�
				if(table.getRowCount()>=0)
				if (table.getRowCount() > 0) { // ���̺��� ���� �ֳ�
					for (int i = 0; i < table.getRowCount(); i++) {
						// ����: ���̺�.getValutAt(i,3)�϶� ������ �� ��
						if (url.getText().equals((String) table.getValueAt(i, 3))) {
							JOptionPane.showMessageDialog(null, "url�� �ߺ��Ǿ����ϴ�.", "url �ߺ� Ȯ��",
									JOptionPane.ERROR_MESSAGE);
							tf = false;
							url.setText("");

						} else {

							tf = true;
							continue;

						}

					}

					if (tf == true) {
						
						number = number + 1;
						label.setText(number + "���� ����Ʈ��  ��ϵǾ��ֽ��ϴ�.");

						sdi = new SiteDetailInfo(name.getText(), url.getText(), id.getText(), passwd.getText(),
								(String) categoryCombo.getSelectedItem(), (String) preferCombo.getSelectedItem(),
								textArea.getText());

						sil.addinfos(sdi);
						sic.addhmap(sil);
						scg.addcate(sic);
						vv.add(sdi);

						table.updateUI();

						name.setText("");
						url.setText("");
						id.setText("");
						passwd.setText("");
						textArea.setText("");
					}

				}

				else {

					number = number + 1;
					label.setText(number + "���� ����Ʈ��  ��ϵǾ��ֽ��ϴ�.");

					sdi = new SiteDetailInfo(name.getText(), url.getText(), id.getText(), passwd.getText(),
							(String) categoryCombo.getSelectedItem(), (String) preferCombo.getSelectedItem(),
							textArea.getText());
					sil = new SiteInfoList(sdi);
					sil.addinfos(sdi);
					sic = new SiteCategory(sil);
					scg=new SiteCategoryList(sic);
		               
		            scg.addcate(sic);
					
					sic.addhmap(sil);
					vv.add(sdi);

					table.updateUI();

					name.setText("");
					url.setText("");
					id.setText("");
					passwd.setText("");
					textArea.setText("");
				}

			} else if (inputButton.getText().equals("����(E)")) {
				int qq = table.getSelectedRow();
				table.setValueAt((String) categoryCombo.getSelectedItem(), qq, 0);
				table.setValueAt((String) preferCombo.getSelectedItem(), qq, 1);
				table.setValueAt((String) name.getText(), qq, 2);
				table.setValueAt((String) url.getText(), qq, 3);

				sdi.seti(idfield.getText());
				sdi.setp(pwfield.getText());

				table.updateUI();

				name.setText("");
				url.setText("");
				id.setText("");
				passwd.setText("");
				textArea.setText("");
				categoryCombo.setSelectedIndex(0);
				preferCombo.setSelectedIndex(0);

				inputButton.setText("�Է�(I)");

			}

		}

	};

	private ActionListener listener7 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			CategoryManageDialog dlg = new CategoryManageDialog(SiteInfoManager.this);
			dlg.setLocation(SiteInfoManager.this.getLocationOnScreen().x + 110,
					SiteInfoManager.this.getLocationOnScreen().y + 70);
			dlg.setVisible(true);
		}
	};

	private ActionListener listener8 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			UserDialog dlg2 = new UserDialog(SiteInfoManager.this);
			dlg2.setLocation(SiteInfoManager.this.getLocationOnScreen().x + 110,
					SiteInfoManager.this.getLocationOnScreen().y + 70);
			dlg2.setVisible(true);
		}
	};

	private JPanel completePanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(createTab(), BorderLayout.CENTER);// ȭ��
		p.add(create(), BorderLayout.WEST);// ȭ��
		p.add(alarm(), BorderLayout.SOUTH); // �Ʒ� â
		return p;
	}

	private JPanel create() { // �Է�/���� ��ü
		JPanel p = new JPanel(new BorderLayout());

		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "�Է�/����"));
		p.add(info(), BorderLayout.NORTH);
		p.add(button(), BorderLayout.CENTER);

		return p;
	}

	private JPanel alarm() {
		JPanel p = new JPanel(new BorderLayout());

		p.setBackground(Color.WHITE);

		SimpleDateFormat format = new SimpleDateFormat("yyyy�� MM��dd�� HH��mm��ss��"); // ��¥ ǥ�� ����

		Calendar time = Calendar.getInstance(); // ��¥ ������ ����

		String format_time = format.format(time.getTime()); // �ð� ������ ����

		JLabel timelabel = new JLabel(format_time + "�� �����߽��ϴ�.");

		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, ""));
		p.add(label, BorderLayout.WEST); // label�� ��� ����Ʈ�� ��ϵǾ�����
		p.add(timelabel, BorderLayout.EAST);
		Font font = new Font("serial", Font.BOLD, 15); // label ��Ʈ �����ϱ�
		label.setFont(font);
		timelabel.setFont(font);
		return p;

	}

	private JPanel info() { // �⺻����+�߰�����
		JPanel p = new JPanel(new BorderLayout());

		p.add(basicInfo(), BorderLayout.NORTH);
		p.add(additionalInfo(), BorderLayout.CENTER);

		return p;
	}

	private JPanel basicInfo() { // �⺻����
		JPanel p = new JPanel(new GridLayout(4, 1));

		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "�⺻ ����"));
		p.setSize(200, 100);
		p.add(name());
		p.add(url());
		p.add(id());
		p.add(passwd());

		return p;
	}

	private JPanel name() { // ����Ʈ��
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("����Ʈ��"));
		p.add(name);

		return p;
	}

	private JPanel url() { // �ּ�
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("�ּ�(URL) http:// "));
		p.add(url);

		return p;
	}

	private JPanel id() { // ���̵�
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("��  ��  ��"));
		p.add(id);

		return p;
	}

	private JPanel passwd() { // ��й�ȣ
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("��й�ȣ"));
		p.add(passwd);

		return p;
	}

	private JPanel additionalInfo() { // �߰�����
		JPanel p = new JPanel(new BorderLayout());
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "�߰� ����"));
		p.setSize(200, 100);
		p.add(north(), BorderLayout.NORTH);
		p.add(memo(), BorderLayout.CENTER);

		return p;
	}

	private JPanel north() { // �޸����� �з� + ��ȣ��
		JPanel p = new JPanel(new GridLayout(2, 1));

		p.setBackground(Color.orange);
		p.add(category());
		p.add(prefer());

		return p;
	}

	private JPanel category() { // �з�
		JPanel p = new JPanel();

		p.setBackground(Color.orange);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("��        ��"));

		p.add(categoryCombo);

		return p;
	}

	private JPanel prefer() { // ��ȣ��
		JPanel p = new JPanel();

		p.setBackground(Color.orange);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("��     ȣ     ��"));

		p.add(preferCombo);

		return p;
	}

	private JPanel memo() { // �޸�
		JPanel p = new JPanel();

		p.setBackground(Color.orange);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("��         ��"));
		p.add(new JScrollPane(textArea));

		return p;
	}

	private JPanel button() { // ��ư 2��
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton graphicButton = new JButton("�׷��Ⱥ���");
	    graphicButton.setToolTipText("�׷��Ⱥ��⸦ ������ �����Ȳ�� �� �� �ֽ��ϴ�.");
		rewriteButton.setMnemonic('N');
		inputButton.setMnemonic('I');
		inputButton.setMnemonic('E');
		graphicButton.setMnemonic('G');
		inputButton.addActionListener(listener3);
		p.add(rewriteButton);
		p.add(inputButton);
		p.add(graphicButton);

		graphicButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.getRowCount() > 0) {
					PieChart chart = new PieChart();
					chart.repaint();
					panel.add(chart, BorderLayout.CENTER);
					DrawingPanel drawingPanel = new DrawingPanel();
					drawingPanel.repaint();
					panel2.add(drawingPanel, BorderLayout.CENTER);

				}

			}

		});

		rewriteButton.addActionListener(new ActionListener() { // �����ۼ� ��ư ������, �ʱ�ȭ�Ǵ� �̺�Ʈ ������

			@Override
			public void actionPerformed(ActionEvent e) {
				name.setText("");
				url.setText("");
				id.setText("");
				passwd.setText("");
				textArea.setText("");

				inputButton.setText("�Է�(I)");
				categoryCombo.setSelectedIndex(0);
				preferCombo.setSelectedIndex(0);
			}

		});

		return p;
	}

	private JTabbedPane createTab() { // �� â
		JTabbedPane p = new JTabbedPane();

		p.addTab("����Ʈ ���", SList());
		p.addTab("�����Ȳ(��)", RStatus());
		p.addTab("�����Ȳ(����)", RStatus2());

		return p;
	}

	private JPanel IDInfo() { // ��������
		JPanel p = new JPanel();

		checkbox = new JCheckBox("�������� ����");
		checkbox = new JCheckBox("�������� ����");
		checkbox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				BufferedWriter bfw;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					try {
						// üũ�ڽ�
						bfw = new BufferedWriter(new FileWriter("idcheckbox.txt", false)); // �����
						bfw.write("true_true"); // üũ�Ǿ� ������ truetrue ��ȯ
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("������ ã�� �� �����ϴ�");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		JLabel iid = new JLabel("���̵�");

		idfield = new JTextField(8);
		JLabel pw = new JLabel("��й�ȣ");
		pwfield = new JTextField(8);

		p.add(checkbox);
		p.add(iid);
		p.add(idfield);
		p.add(pw);
		p.add(pwfield);

		return p;
	}

	private JPanel DeleteBtn() { // ������ư
		JPanel p = new JPanel();
		delete.setEnabled(false);
		p.add(delete);

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "���� �����Ͻðڽ��ϱ�?", "����", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					number = number - 1; // ����Ʈ ���� -1
					label.setText(number + "���� ����Ʈ��  ��ϵǾ��ֽ��ϴ�.");
					// ����Ʈ ����

					int rowIndex = table.getSelectedRow();

					// ���� ���ϰ� ���� ��� ���ϰ� -1
					if (result == -1)
						return;

					vv.remove(rowIndex);
					infotableModel.fireTableDataChanged();
					table.updateUI();
					if(table.getRowCount()==0) {
						delete.setEnabled(false);
					}
					// ������ �����
				}

			}

		});
		return p;
	}

	private JPanel Tab_Bottom() { // �������� + ������ư
		JPanel p = new JPanel(new BorderLayout());

		p.add(IDInfo(), BorderLayout.WEST);
		p.add(DeleteBtn(), BorderLayout.EAST);

		return p;
	}

	private JPanel SList() { // ����Ʈ ��� ��
		JPanel p = new JPanel(new BorderLayout());

		p.add(SList_North(), BorderLayout.NORTH);
		p.add(SList_Center(), BorderLayout.CENTER);
		p.add(Tab_Bottom(), BorderLayout.SOUTH);

		return p;
	}

	private JPanel SList_North() {
		JPanel p = new JPanel(new BorderLayout());
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "�˻�/����"));

		p.add(createSearch(), BorderLayout.WEST);
		p.add(createSort(), BorderLayout.CENTER);

		return p;
	}

	private JPanel createSort() { // ����
		JPanel p = new JPanel(new FlowLayout());

		p.setBackground(Color.GREEN);
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "����"));
		p.setSize(200, 100);
		p.add(CategorizeCombo);
		p.add(SiteCombo);
		p.add(SortButton);
		SortButton.setToolTipText(" ! ������ �� �ٽ� �⺻��ư�� �����ֽʽýÿ� !");
		p.add(SortDefault);

		SortButton.addActionListener(new ActionListener() {
			int q = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				String a = (String) CategorizeCombo.getSelectedItem();
				String b = (String) SiteCombo.getSelectedItem();

				for (int j = 0; j < vv.size(); j++) {
					vvv[j] = vv.get(j);
				}
				if (b == "��ȣ��") {
					for (int u = 0; u < vv.size(); u++) {
						for (int x = 0; x < vv.size() - u - 1; x++) {
							if (vvv[x].getPrefer().length() > vvv[x + 1].getPrefer().length()) {
								SiteDetailInfo site = vvv[x];
								vvv[x] = vvv[x + 1];
								vvv[x + 1] = site;
							}
						}
					}

					for (int i = 0; i < vv.size(); i++) {
						if (vvv[i].getCategory() == a) {
							f.add(vvv[i]);

						} else {
							fa.add(vv.get(i));

						}
					}

				}
				if (b == "����Ʈ �̸�") {
					for (int u = 0; u < vv.size(); u++) {
						for (int x = 0; x < vv.size() - u - 1; x++) {
							if (vvv[x].getname().charAt(0) >= vvv[x + 1].getname().charAt(0)) {
								SiteDetailInfo site = vvv[x];
								vvv[x] = vvv[x + 1];
								vvv[x + 1] = site;
							}
						}
					}
					for (int i = 0; i < vv.size(); i++) {
						if (vvv[i].getCategory() == a) {
							f.add(vvv[i]);

						} else {
							fa.add(vv.get(i));

						}
					}

				}
				if (b == "����Ʈ �ּ�") {
					for (int u = 0; u < vv.size(); u++) {
						for (int x = 0; x < vv.size() - u - 1; x++) {
							if (vvv[x].getu().charAt(0) >= vvv[x + 1].getu().charAt(0)) {
								SiteDetailInfo site = vvv[x];
								vvv[x] = vvv[x + 1];
								vvv[x + 1] = site;
							}
						}
					}

					for (int i = 0; i < vv.size(); i++) {
						if (vvv[i].getCategory() == a) {
							f.add(vvv[i]);

						} else {
							fa.add(vv.get(i));

						}
					}
				}

				for (int p = 0; p < fa.size(); p++) {
					f.add(fa.get(p));
				}
				table.setModel(new InfoTableModel(f)); // ss �� �˻� �гο��� �޺��ڽ��� �ؽ�Ʈ�ʵ� ���͸��ؼ� ��Ƶ� ����

				table.updateUI();
			}

		});

		SortDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				table.setModel(new InfoTableModel(vv));
				// ����: infos �� ���� ������ �ٽ� �ҷ��´�.

				table.updateUI();
				s.clear();
				f.clear();
				fa.clear();

			}

		});

		return p;
	}

	int q = 0;

	private JPanel createSearch() { // �˻�
		JPanel p = new JPanel(new FlowLayout());
		p.setBackground(Color.GREEN);
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "�˻�"));
		p.add(SearchCombo);
		p.add(new JLabel("����: "));
		p.add(SearchFilter);

		SearchFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String com = (String) SearchCombo.getSelectedItem();
				String text = SearchFilter.getText();

				for (int i = 0; i < sic.infoArray1.size(); i++) {
					if (com == sic.infoArray1.get(i).getCategory()) {
						if ((sic.infoArray1.get(i).name).contains(text) == true
								|| (sic.infoArray1.get(i).url).contains(text) == true) {
							s.add(sic.infoArray1.get(i));
						}

					}
				}
				table.setModel(new InfoTableModel(s));

				table.updateUI();

			}
		});

		return p;
	}

	private JPanel SList_Center() { // ����
		JPanel p = new JPanel(new BorderLayout());

		infotableModel = new InfoTableModel(vv);
		table = new JTable(infotableModel);
		table.setAutoCreateRowSorter(true);
		// ����: ���̺� ����κ��� ������ �ڵ����� ���ı�� �߰�

		table.setRowHeight(30);
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				idfield.setEditable(true);
				pwfield.setEditable(true);
				delete.setEnabled(true);
				
				tablerow = table.getSelectedRow();
				inputButton.setText("����(E)"); // ���� ��ư���� ����

				table = (JTable) e.getComponent();
				infotableModel = (InfoTableModel) table.getModel();

				checkbox.addItemListener(new ItemListener() { // ������������ üũ�ڽ�����

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) { // ������������ üũ�ڽ� ����
							idfield.setText(vv.get(tablerow).geti()); // ǥ�� �Է��� id�� �ؽ�Ʈ�ʵ忡 ǥ��
							pwfield.setText(vv.get(tablerow).getp()); // ǥ�� �Է��� pw�� �ؽ�Ʈ�ʵ忡 ǥ��
						} else { // ������������ üũ�ڽ� �̼���
							idfield.setText(""); // �ƹ��͵� �� ����
							pwfield.setText("");
						}
					}

				});
				// ǥ�� �Է��� url,name�� �ؽ�Ʈ�ʵ忡 ǥ��
				url.setText(vv.get(tablerow).getu());
				name.setText(vv.get(tablerow).getname());

				// ǥ�� �Է��� memo,combobox�� �ؽ�Ʈ�ʵ忡 ǥ��
				textArea.setText(vv.get(tablerow).getm());
				categoryCombo.setSelectedItem(vv.get(tablerow).getCategory());
				preferCombo.setSelectedItem(vv.get(tablerow).getPrefer());
			}
		});
		p.add(new JScrollPane(table), BorderLayout.CENTER);

		return p;
	}

	private JPanel RStatus() { // �����Ȳ ��
		panel = new JPanel(new BorderLayout()); // ����ִ� �����Ȳ �гθ� ����� ����
		return panel;
	}

	class Slice { // ������ ���� ����ִ� slice Ŭ����
		double value;
		Color color;

		public Slice(double value, Color color) {
			this.value = value;
			this.color = color;
		}
	}

	class PieChart extends JComponent { // �����Ȳ �� - ��
		// hashmap ������ŭ slice�� ����
		// slice���� �ٸ� ��, �ٸ� ����
		// �����ϴ°� �߰��ؾ���
		Slice[] slices = new Slice[categoryCombo.getItemCount()]; // �з� ũ�⸸ŭ slices �����

		int count = 0; // for���� ������ ǥ�� �з��� �ϳ��� ������ categorylist ���� ���� �ϳ��� ���Ѵ�.
		String value;
		double[] ccategoryname = new double[categoryCombo.getItemCount()]; // ������ ����ϱ� ���� double ���
		int total;

		public void paint(Graphics g) { // �׸���
			for (int i = 0; i < categoryCombo.getItemCount(); i++) {
				ccategoryname[i] = 0; // �з��� ���� 0�� ����ִ´�
			}
			for (int i = 0; i < table.getRowCount(); i++) {
				value = (String) table.getValueAt(i, 0); //
				for (int j = 0; j < categoryCombo.getItemCount(); j++) {
					String categoryname = categoryCombo.getItemAt(j);
					if (value.equals(categoryname) == true) { // ǥ�� �з� ī�װ��� ������
						(ccategoryname[j]) += 1; // �з��� ���� ī��Ʈ ���� ���Ѵ�
					}

				}
			}

			for (int i = 0; i < ccategoryname.length; i++) {
				total += ccategoryname[i]; // ī��Ʈ���� ��� ���Ѵ�
			}

			for (int i = 0; i < ccategoryname.length; i++) {
				if (ccategoryname[i] != 0) {
					ccategoryname[i] = (ccategoryname[i] * 100) / total; // ������ ccategoryname[i]�� �����Ѵ�.
				}
			}

			for (int x = 0; x < ccategoryname.length; x++) { // categoryList�� �ٲ�
				Color color = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0),
						(int) (Math.random() * 255.0)); // ���� �������� �����

				slices[x] = new Slice(ccategoryname[x], color); // slice�� ������ ���� �����Ѵ�

			}
			drawPie((Graphics2D) g, getBounds(), slices);
		}

		void drawPie(Graphics2D g, Rectangle area, Slice[] slices) {
			double total = 0.0D;
			for (int i = 0; i < slices.length; i++) {
				total += slices[i].value; // �����̽� ���� �� ���Ѵ�
			}
			double curValue = 0.0D;
			int startAngle = 0;
			for (int i = 0; i < slices.length; i++) {
				startAngle = (int) (curValue * 360 / total); // ��ü 360������ �����ϴ� ����
				int arcAngle = (int) (slices[i].value * 360 / total); // �׸��� ����
				g.setColor(slices[i].color);
				g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);
				curValue += slices[i].value;
			}
		}
	}

	private JPanel RStatus2() { // �����Ȳ �� - ����׷���
		panel2 = new JPanel(new BorderLayout());
		return panel2;
	}

	class DrawingPanel extends JPanel {
		public void paint(Graphics g) {
			int total = 0;
			String value;
			g.clearRect(0, 0, getWidth(), getHeight());
			g.drawLine(50, 250, 450, 250); // ���� ��
			for (int cnt = 1; cnt < 11; cnt++) { // �������� ���� �׾���´� �뷫 10����
				g.drawLine(50, 250 - 20 * cnt, 450, 250 - 20 * cnt);
			}

			g.drawLine(50, 20, 50, 250); // ���� ��

			for (int i = 0; i < categoryCombo.getItemCount(); i++) {
				g.drawString(categoryCombo.getItemAt(i), 70 * (i + 1), 270); // �з����� �۾� ��Ÿ����
				Color color = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0),
						(int) (Math.random() * 255.0));
				g.setColor(color);
			}

			int[] ccategoryname = new int[categoryCombo.getItemCount()];
			for (int i = 0; i < categoryCombo.getItemCount(); i++) {

				ccategoryname[i] = 0;
			}
			for (int i = 0; i < table.getRowCount(); i++) {
				value = (String) table.getValueAt(i, 0);
				for (int j = 0; j < categoryCombo.getItemCount(); j++) {
					String categoryname = categoryCombo.getItemAt(j);
					if (value.equals(categoryname) == true) {
						(ccategoryname[j]) += 1;
					}

				}
			}

			for (int i = 0; i < ccategoryname.length; i++) {
				total += ccategoryname[i];
			}

			for (int i = 0; i < ccategoryname.length; i++) {
				if (ccategoryname[i] != 0) {
					ccategoryname[i] = (ccategoryname[i] * 100) / total;
				}
			}

			for (int i = 0; i < categoryCombo.getItemCount(); i++) {
				if (ccategoryname[i] > 0) {
					g.fillRect((i + 1) * 70 + 10, 250 - ccategoryname[i] * 2, 10, ccategoryname[i] * 2); // ���� �׸���
				}
			}

		}

	}

	public class SiteInfo {
		private String name;
		private String url;
		private String id;
		private String passwd;

		public SiteInfo(String name, String url, String id, String passwd) {
			this.name = name;
			this.url = url;
			this.id = id;
			this.passwd = passwd;
		}

		public void setname(String name) {
			this.name = name;
		}

		public void setu(String url) {
			this.url = url;
		}

		public void seti(String id) {
			this.id = id;
		}

		public void setp(String passwd) {
			this.passwd = passwd;
		}

		public String getname() {
			return name;
		}

		public String getu() {
			return url;
		}

		public String geti() {
			return id;
		}

		public String getp() {
			return passwd;
		}
	}

	public class SiteDetailInfo extends SiteInfo {

		private String name;
		private String url;
		private String id;
		private String passwd;
		private String category;
		private String prefer;
		private String memo;

		public SiteDetailInfo(String name, String url, String id, String passwd, String category, String prefer,
				String memo) {
			super(name, url, id, passwd);
			this.category = category;
			this.prefer = prefer;
			this.memo = memo;
			this.name = name;
			this.url = url;
			this.id = id;
			this.passwd = passwd;

		}

		public String getname() {
			return name;
		}

		public void setname(String name) {
			this.name = name;
		}

		public void setu(String url) {
			this.url = url;
		}

		public String getu() {
			return url;
		}

		public String geti() {
			return id;
		}

		public String getp() {
			return passwd;
		}

		public void setm(String memo) {
			this.memo = memo;
		}

		public String getm() {
			return memo;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getCategory() {
			categoryCombo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JComboBox cb = (JComboBox) e.getSource(); // �޺��ڽ� �˾Ƴ���
					String category = (String) cb.getSelectedItem();// ���õ� �������� �ε���

				}
			});
			return category;
		}

		public void setPrefer(String prefer) {
			this.prefer = prefer;
		}

		public String getPrefer() {
			preferCombo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JComboBox cb = (JComboBox) e.getSource(); // �޺��ڽ� �˾Ƴ���
					String category = (String) cb.getSelectedItem();// ���õ� �������� �ε���

				}
			});
			return prefer;
		}

	}

	public class SiteInfoList {
		Vector<SiteDetailInfo> infos = new Vector<SiteDetailInfo>();

		public SiteInfoList(SiteDetailInfo di) {
		}

		public SiteDetailInfo getinfos(int i) {
			return infos.get(i);
		}

		public int getsizev() {
			return infos.size();
		}

		public void addinfos(SiteDetailInfo di) {
			infos.add(di);
		}

	}

	public class SiteCategory {
		HashMap<Integer, String> hmap = new HashMap<Integer, String>();

		ArrayList<SiteDetailInfo> infoArray1 = new ArrayList<SiteDetailInfo>();
		int i = 0;

		public SiteCategory(SiteInfoList s) {
		}

		public void seth(String s) {
			hmap.put(i, s);
			i++;

		}

		public void addhmap(SiteInfoList s) {
			hmap.put(i, s.infos.get(i).category);
			infoArray1.add(s.infos.get(i));
			i++;
		}
	}

	public class SiteCategoryList {
		ArrayList<String> categorylist = new ArrayList<String>();

		public SiteCategoryList(SiteCategory s) {
		}

		public void addcate(SiteCategory s) {
			categorylist.add(s.hmap.get(0));
		}

	}

	class InfoTableModel extends AbstractTableModel {

		private Vector<String> mColumnNames;
		Vector<SiteDetailInfo> data;

		public InfoTableModel(Vector vv) {// Vector<Score1> vData
			String[] s = { "�з�", "��ȣ��", "����Ʈ �̸�", "����Ʈ �ּ�" };

			mColumnNames = new Vector<String>(s.length);

			for (int i = 0; i < s.length; i++)
				mColumnNames.addElement(s[i]);

			data = vv;

		}

		public void removeAllRow() {
			data.clear();
		}

		@Override
		public int getColumnCount() { // ���� ��� �˾Ƴ���

			return mColumnNames.size();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			// data�� �ؽ����̱⶧���� ����Ʈ�� ��ȯ���ش�.
			SiteDetailInfo user = (SiteDetailInfo) data.get(row);

			switch (col) {

			case 0:
				return user.getCategory();
			case 1:
				return user.getPrefer();
			case 2:
				return user.getname();
			case 3:
				return user.getu();

			}
			return null;

		}

		@Override
		public String getColumnName(int column) {
			return mColumnNames.get(column);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public void setValueAt(Object aValue, int row, int columnIndex) {
			SiteDetailInfo student = (SiteDetailInfo) data.get(row);

			switch (columnIndex) {
			case 0:
				student.setCategory((String) aValue);
				table.updateUI();
				break;
			case 1:
				student.setPrefer((String) aValue);
				table.updateUI();

				break;
			case 2:
				student.setname((String) aValue);
				table.updateUI();
				break;
			case 3:
				student.setu((String) aValue);
				table.updateUI();
				break;

			}

		}

	}

	class LoginDialog extends JDialog {
		private JLabel pw = new JLabel("��й�ȣ ");
		private JTextField pwt = new JTextField(10);

		private JButton loginButton = new JButton("�α���");
		private JButton cancelButton = new JButton("����");

		ArrayList<Integer> id2;
		UserDialog i2 = new UserDialog(null);

		public LoginDialog(JFrame frame, String title) {
			super(frame, title, true);
			add(buildlogin());

			setLocation(400, 400);
			setSize(250, 180);
			// x������ ����ȵ�
			setDefaultCloseOperation(LoginDialog.DO_NOTHING_ON_CLOSE);
		}

		private JPanel LoginDialog_Input() {
			JPanel p = new JPanel();
			p.setBackground(Color.WHITE);
			p.add(pw);
			p.add(pwt);

			return p;
		}

		private JPanel LoginDialog_Button() {
			JPanel p = new JPanel();
			p.setBackground(Color.WHITE);
			p.add(loginButton);
			p.add(cancelButton);
			loginButton.addActionListener(login_cancel_action);
			cancelButton.addActionListener(login_cancel_action);

			return p;

		}

		private JPanel Explain() {
			JPanel p = new JPanel(new FlowLayout());
			JLabel ex = new JLabel("�ʱ� ��й�ȣ�� 1217�Դϴ�.");
			p.setBackground(Color.WHITE);
			p.add(ex);
			return p;
		}

		private JPanel buildlogin() {
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.WHITE);
			p.setBorder(new TitledBorder(new LineBorder(Color.gray), "�ȳ��ϼ���?"));
			p.add(Explain(), BorderLayout.SOUTH);
			p.add(LoginDialog_Button(), BorderLayout.CENTER);
			p.add(LoginDialog_Input(), BorderLayout.NORTH);

			return p;
		}

		ActionListener login_cancel_action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = pwt.getText();

				// UserDialog���� ArrayList ��й�ȣ ������ �����´�. !!!!
				// �Է��ߴ� �Ͱ� ArrayList�� ������ �ִ� ���� ���Ѵ�.
				// �ִ� ���� �Է����� �� false�� �ǰ� �Ѵ�.
				id2 = i2.idList;
				if (e.getSource() == loginButton) {
					for (int i = 0; i < id2.size(); i++) {
						if (s.equals(id2.get(i).toString()))
							setVisible(false);
					}
				} else if (e.getSource() == cancelButton) {
					LoginDialog.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					System.exit(0);
				}
			}
		};
	}

//����: ����� ���̾�α�
	public class UserDialog extends JDialog {

		JTextField editText = new JTextField(10);
		JTextField userid = new JTextField(10);
		JButton deleteButton = new JButton("����� ����");
		JButton addButton = new JButton("����� �߰�");

		JList<String> list = new JList<String>(new DefaultListModel<String>());
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		String firstid = "1217";// �ʱ� ��й�ȣ

		// ����: ���Ͽ��� �а� ����
		// ����: ObjectOutputStream, FileOutputStream --> ���Ͽ� ����
		// ����: FileInputStream, ObjectInputStream --> ArrayList<Integer>���Ͽ��� ����
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		FileInputStream fin = null;
		ObjectInputStream oin = null;

		public UserDialog(JFrame f) {

			super(f, "����� ����", true);

			buildGUI();

			this.setSize(500, 300);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			try {

				// ����: �б�
				fin = new FileInputStream("LoginList.txt");
				oin = new ObjectInputStream(fin);

				// ����: �������� �迭�� �ٽ� ����ȯ�ؼ� idList�� �ֱ�
				idList = (ArrayList) oin.readObject();

				// ����: �𵨿� ���� �߰��ϱ�
				// ����: idList�� size��ŭ �ݺ�
				for (int i = 0; i < idList.size(); i++) {
					model.addElement(idList.get(i).toString());
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					fin.close();
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void buildGUI() {
			setLayout(new BorderLayout());
			add(explain(), BorderLayout.NORTH);
			add(content(), BorderLayout.CENTER);
			add(edit(), BorderLayout.SOUTH);

		}

		private JPanel explain() {
			JPanel p = new JPanel(new FlowLayout());
			JLabel ex = new JLabel("��й�ȣ�� 4�ڸ� ���ڷ� �̷�����ϴ�.\n�ؽ�Ʈâ�� ��ȣ�� �Է��ϰ� ������ư�� ������ �����˴ϴ�.");
			p.add(ex);
			return p;
		}

		private JPanel content() {

			JPanel p = new JPanel(new GridLayout(1, 1));

			Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
			p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "����׸�"));

			p.add(new JScrollPane(list));

			return p;
		}

		private JPanel edit() {

			JPanel p = new JPanel(new FlowLayout());
			p.add(userid);
			p.add(addButton);
			p.add(deleteButton);
			addButton.addActionListener(editlistener);
			deleteButton.addActionListener(editlistener);
			return p;
		}

		ActionListener editlistener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String s = userid.getText();
				if (e.getSource() == deleteButton) {

					// ����: �����ͷ����� �̿�
					Iterator<Integer> it = idList.iterator();
					while (it.hasNext()) {
						Integer s1 = it.next();
						// ����: s1�� userid�� ������
						if (s1 == Integer.parseInt(userid.getText()))
							it.remove();

					}
					model.removeElement(s);
					try {

						// ����: LoginList�� �����ϱ�
						fout = new FileOutputStream("LoginList.txt", true);
						oout = new ObjectOutputStream(fout);
						// ����: ����
						oout.writeObject(idList);
						oout.reset();
					} catch (Exception e1) {
						System.out.println(e1);
					} finally {
						try {
							fout.close();
							oout.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					userid.setText("");
				} else if (e.getSource() == addButton) {

					// ����: IntegerŸ���̴ϱ� ����ȯ�ؼ� idList�� �־��ֱ�
					idList.add(Integer.parseInt(s));
					model.addElement(s);
					try {
						// ����: FileOutputStream���� ���� �����ϱ�
						fout = new FileOutputStream("LoginList.txt");
						oout = new ObjectOutputStream(fout);
						// ����: ����
						oout.writeObject(idList);
						oout.reset();
					} catch (Exception e1) {
						System.out.println(e1);
					} finally {
						try {
							fout.close();
							oout.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					userid.setText("");
				}
			}
		};
	}

	class CategoryManageDialog extends JDialog {

		JTextField editText = new JTextField(10);
		JButton deleteButton;
		JButton addButton;
		JButton newButton;
		JButton inputcompleteButton;
		JButton deletecompleteButton;

		JList<String> list = new JList<String>(new DefaultListModel<String>());
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();

		public CategoryManageDialog(JFrame f) {
			super(f, "����", true);

			for (int i = 0; i < CategorizeCombo.getItemCount(); i++) {
				model.addElement(CategorizeCombo.getItemAt(i));
			}
			buildGUI();

			this.setSize(650, 300);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		}

		private void buildGUI() {
			setLayout(new GridLayout(1, 2));
			add(registeredItems());
			add(EditContent());

		}

		private JPanel EditContent() {
			JPanel p = new JPanel(new BorderLayout());

			p.add(EditPanel(), BorderLayout.CENTER);
			p.add(EditButton(), BorderLayout.SOUTH);

			return p;
		}

		private JPanel EditPanel() { // ���� ����

			JPanel p = new JPanel(new BorderLayout());

			addButton = new JButton("�߰�(A)");
			addButton.setToolTipText("�߰� �� �߰��Ϸ� ��ư�� ��������!");
			addButton.setMnemonic('A');
			deleteButton = new JButton("����(D)");
			deleteButton.setToolTipText("���� �� �����Ϸ� ��ư�� ��������!");
			deleteButton.setMnemonic('D');

			Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
			p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "���� ����"));

			p.add(EditPanelName(), BorderLayout.NORTH);
			p.add(EditPanelButton(), BorderLayout.CENTER);

			deleteButton.addActionListener(delete_add_button);
			addButton.addActionListener(delete_add_button);

			return p;

		}

		ActionListener delete_add_button = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String s = editText.getText();

				String sasa = list.getSelectedValue();
				if (e.getSource() == deleteButton) {
					CategorizeCombo.removeItem(sasa);

					categoryCombo.removeItem(sasa);
					SearchCombo.removeItem(sasa);
					model.removeElement(list.getSelectedValue());
					model.removeElement(s);
					editText.setText("");
				} else if (e.getSource() == addButton) {

					model.addElement(s);

					editText.setText("");
				}
			}
		};

		private JPanel EditPanelName() {
			JPanel p = new JPanel(new FlowLayout());
			p.add(new JLabel("�׸� �̸�"));
			p.add(editText);
			return p;
		}

		private JPanel EditPanelButton() {
			JPanel p = new JPanel(new FlowLayout());
			p.add(addButton);
			p.add(deleteButton);
			return p;
		}

		private JPanel EditButton() { // �ű� �Ϸ� ��ư
			JPanel p = new JPanel(new FlowLayout());

			newButton = new JButton("�ű�(N)");
			newButton.setMnemonic('N');
			inputcompleteButton = new JButton("�߰��Ϸ�(P)");
			inputcompleteButton.setMnemonic('P');
			deletecompleteButton = new JButton("�����Ϸ�(C)");
			newButton.addActionListener(inputlistener);
			inputcompleteButton.addActionListener(inputlistener);
			deletecompleteButton.addActionListener(deletelistener);

			p.add(newButton);
			p.add(inputcompleteButton);
			p.add(deletecompleteButton);

			return p;
		}

		ActionListener inputlistener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = editText.getText();
				if (e.getSource() == newButton) {
					model.removeAllElements();
					list.setModel(model);

				} else if (e.getSource() == inputcompleteButton) {
					CategorizeCombo.addItem(model.elementAt(model.getSize() - 1));
					categoryCombo.addItem(model.elementAt(model.getSize() - 1));
					SearchCombo.addItem(model.elementAt(model.getSize() - 1));

					setVisible(false);
				}
			}
		};

		ActionListener deletelistener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == newButton) {
					model.removeAllElements();
					list.setModel(model);

				} else if (e.getSource() == deletecompleteButton) {
					CategorizeCombo.removeItem(list.getSelectedValue());
					categoryCombo.removeItem(list.getSelectedValue());
					SearchCombo.removeItem(list.getSelectedValue());

					setVisible(false);
				}
			}
		};

		private JPanel registeredItems() {

			JPanel p = new JPanel(new GridLayout(1, 1));

			Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
			p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "����׸�"));

			p.add(new JScrollPane(list));

			return p;
		}

	}

}