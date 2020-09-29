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
	// 정렬분류 전역변수
	// 테이블생성때 쓰이는 data변수를 arraylist로 바꾼 것.
	public String[] SearchSelected = { "일반", "쇼핑", "포털", "학교" }; // 검색선택 전역변수
	private String[] SortCategorize_mini = { "사이트 이름", "사이트 주소", "선호도" }; // 정렬 사이트 전역변수 // SortCategorize2로 수정
	private String[] SortCtegorize1 = { "전체", "일반", "쇼핑", "포털", "학교" };
	private JTextField SearchFilter = new JTextField(8);

	private JComboBox<String> CategorizeCombo = new JComboBox<String>(SearchSelected);
	private JComboBox<String> SearchCombo = new JComboBox<String>(SortCtegorize1);
	private JComboBox<String> SiteCombo = new JComboBox<String>(SortCategorize_mini);
	String[] categoryMenu = { "일반", "쇼핑", "포털", "학교" };
	String[] preferMenu = { "☆☆☆☆☆", "☆☆☆☆", "☆☆☆", "☆☆", "☆" };
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
	JLabel label = new JLabel(number + "개의 사이트가  등록되어있습니다.");
	
	private LoginDialog logindialog;

	private JButton SortButton = new JButton("정렬");
	private JButton SortDefault = new JButton("기본");

	JTextArea textArea = new JTextArea(6, 16);
	JTextField name = new JTextField("", 10);
	JTextField url = new JTextField("", 8);
	JTextField id = new JTextField("", 6);
	JTextField passwd = new JTextField("", 6);
	private JButton rewriteButton = new JButton("새로 작성(N)");
	private JButton inputButton = new JButton("입력(I)");
	JMenuItem item8 = new JCheckBoxMenuItem("자동 로그인(L)");
	JMenuItem item9 = new JCheckBoxMenuItem("계정정보 보기 상태 기억하기(V)");
	JButton delete = new JButton("삭제");
	
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

		super("웹 계정관리");
		createMenu();

		setSize(850, 600);
		setLocation(500, 200);
		add(completePanel());
		setVisible(true);

		logindialog = new LoginDialog(this, "비밀번호 로그인");

		Point p = SiteInfoManager.this.getLocationOnScreen();
		int cx = SiteInfoManager.this.getWidth() / 2;
		int cy = SiteInfoManager.this.getHeight() / 2;
		logindialog.setLocation(p.x + cx - logindialog.getWidth() / 2, p.y + cy - logindialog.getHeight() / 2);

		BufferedReader bfr;
		try {
			// 체크박스
			bfr = new BufferedReader(new FileReader("logincheck.txt"));
			String line = ""; // 아무 것도 없음
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
			System.out.println("파일을 찾을 수 없습니다");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader bfr2;
		try {
			// 체크박스
			bfr2 = new BufferedReader(new FileReader("idcheckbox.txt"));
			String line = ""; // 아무 것도 없음
			while ((line = bfr2.readLine()) != null) {
				if (line.equals("true_true")) { // 계정정보보기상태 체크, 계정정보보기 체크
					checkbox.setSelected(true); // 계정정보보기 체크
					item9.setSelected(true); // 계정정보보기상태 체크
				} else if (line.equals("true")) { // 계정정보보기상태 체크, 계정정보보기 미체크
					item9.setSelected(true); // 계정정보보기상태 체크
				}
			}
			bfr2.close();
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다");
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

	public void Close() { // 종료 처리
		int exitOption = JOptionPane.showConfirmDialog(SiteInfoManager.this, "정말 종료하시겠습니까?", "종료 확인",
				JOptionPane.YES_NO_OPTION);

		if (exitOption == JOptionPane.YES_OPTION) {
			System.exit(JFrame.EXIT_ON_CLOSE); // 프레임을 종료
		} else if ((exitOption == JOptionPane.NO_OPTION) || (exitOption == JOptionPane.CLOSED_OPTION)) {
			return; // 아무 작업 없이 다이얼로그 상자를 닫는다
		}
	}

	private void createMenu() {
		// 보경: 3번
		// 보경: 메뉴 구성에 맞게 구현
		JMenu fileMenu = new JMenu("파일(F)");
		JMenu manageMenu = new JMenu("관리(M)");
		JMenu settingMenu = new JMenu("설정(S)");
		JMenu HelpMenu = new JMenu("도움말(H)");

		JMenu subMenu = new JMenu("테마(T)");
		settingMenu.add(subMenu);
		JMenuItem subitem1 = new JMenuItem("테마변경");

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
		JMenuItem item10 = new JMenuItem("설명서(U)");
		HelpMenu.add(item10);
		item10.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 JOptionPane.showMessageDialog(null, "1. 테이블 헤더 부분의 글자를 누르면 자동정렬 됩니다.\n2. "
				 		+ "입력 시 사이트이름과 사이트 주소는 반드시 적어주세요\n3. 등록현황을보시려면 반드시 그래픽보기버튼을 눌러주세요.", "사용 설명서",
						JOptionPane.INFORMATION_MESSAGE);
			}

		});

		JMenuItem item1 = new JMenuItem("엑셀 파일에서 가져오기(I)");
		item1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				// 테이블 삭제하기
				TableModel model = table.getModel();
				((InfoTableModel) model).removeAllRow();
				
				Workbook workbook = null;
				Sheet sheet = null;

				String excelFile = "excel2.xls";
				try {
					// 엑셀파일을 인식
					workbook = Workbook.getWorkbook(new File(excelFile));

					// 엑셀파일에 포함된 sheet의 배열을 리턴한다.
					// workbook.getSheets();

					if (workbook != null) {
						// 엑셀파일에서 첫번째 Sheet를 인식
						sheet = workbook.getSheet(0);

						if (sheet != null) {
							// 셀인식 Cell a1 = sheet.getCell( 컬럼 Index, 열 Index);
							// 셀 내용 String stringa1 = a1.getContents();

							// 기록물철의 경우 실제 데이터가 시작되는 Row지정
							int nRowStartIndex = 0;
							// 기록물철의 경우 실제 데이터가 끝 Row지정
							int nRowEndIndex = sheet.getColumn(2).length - 1;

							// 기록물철의 경우 실제 데이터가 시작되는 Column지정
							int nColumnStartIndex = 0;
							// 기록물철의 경우 실제 데이터가 끝나는 Column지정
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
										// 나영: siteDetailInfo 벡터에 객체를 저장하고 다시 테이블 모델을 생성한다.
									}
									i++;

								}
							}
							table.setModel(new InfoTableModel(vv));
							table.updateUI();
						}

						else {
							System.out.println("Sheet가 비었습니다.!");
						}
					} else {
						System.out.println("WorkBook가 비었습니다!");
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
		JMenuItem item2 = new JMenuItem("엑셀 파일로 내보내기(E)...");
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
		JMenuItem item3 = new JMenuItem("저장(S)");
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

		JMenuItem item4 = new JMenuItem("로그아웃(O)");
		item4.setMnemonic('O');
		fileMenu.add(item4);
		item4.addActionListener(listener4);

		JMenuItem item5 = new JMenuItem("종료(X)...");
		item5.addActionListener(listener5);
		item5.setMnemonic('X');
		fileMenu.add(item5);

		JMenuItem item6 = new JMenuItem("사용자(U)");
		item6.setMnemonic('U');
		item6.addActionListener(listener8);

		JMenuItem item7 = new JMenuItem("사이트 분류(C)");
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
						// 체크박스
						bfw = new BufferedWriter(new FileWriter("logincheck.txt", false)); // 덮어쓰기
						bfw.write("true"); // 체크되어 있으면 true 반환
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("파일을 찾을 수 없습니다");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else
					try {
						// 체크박스
						bfw = new BufferedWriter(new FileWriter("logincheck.txt", false)); // 덮어쓰기
						bfw.write("false"); // 체크 없으면 false 반환
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("파일을 찾을 수 없습니다");
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
				if (e.getStateChange() == ItemEvent.SELECTED) { // 계정정보보기상태가 선택되어있으면
					try {
						// 체크박스
						bfw = new BufferedWriter(new FileWriter("idcheckbox.txt", false)); // 덮어쓰기
						bfw.write("true");
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("파일을 찾을 수 없습니다");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else // 계정정보 보기상태 미선택
					try {
						// 체크박스
						bfw = new BufferedWriter(new FileWriter("idcheckbox.txt", false)); // 덮어쓰기
						bfw.write("false"); // 체크 없으면 false 반환
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("파일을 찾을 수 없습니다");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}

		});
		item9.setMnemonic('V');
		settingMenu.add(item8);
		settingMenu.add(item9);

		setJMenuBar(mb); // MB : 메뉴바

	}

	private ActionListener listenerThema = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String themaName = e.getActionCommand();
			if (themaName.equals("테마변경")) {
				JFrame.setDefaultLookAndFeelDecorated(true);
				// 보경:Look & Feel을 사용한다는 것
				try {
					// 보경:JFrame.setDefaultLookAndFeelDecorated(true);
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) { // nimbus 테마
							UIManager.setLookAndFeel(info.getClassName());

							break;
						}
					}
					// 보경:에러처리
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
			logindialog = new LoginDialog(SiteInfoManager.this, "사용자 로그인");

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
			int result = JOptionPane.showConfirmDialog(null, "정말 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				System.exit(0);
			}
		}
	};

// url중복 JOptionPane

	private ActionListener listener3 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (inputButton.getText().equals("입력(I)")) {
				// 보경: tf겂을 false로 초기화
				boolean tf = false;

				// 보경: 처음 테이블이 없을 때, getRowCount>0이상일때
				if(table.getRowCount()>=0)
				if (table.getRowCount() > 0) { // 테이블의 값이 있나
					for (int i = 0; i < table.getRowCount(); i++) {
						// 보경: 테이블.getValutAt(i,3)일때 각각의 값 비교
						if (url.getText().equals((String) table.getValueAt(i, 3))) {
							JOptionPane.showMessageDialog(null, "url이 중복되었습니다.", "url 중복 확인",
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
						label.setText(number + "개의 사이트가  등록되어있습니다.");

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
					label.setText(number + "개의 사이트가  등록되어있습니다.");

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

			} else if (inputButton.getText().equals("수정(E)")) {
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

				inputButton.setText("입력(I)");

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
		p.add(createTab(), BorderLayout.CENTER);// 화진
		p.add(create(), BorderLayout.WEST);// 화진
		p.add(alarm(), BorderLayout.SOUTH); // 아래 창
		return p;
	}

	private JPanel create() { // 입력/수정 전체
		JPanel p = new JPanel(new BorderLayout());

		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "입력/수정"));
		p.add(info(), BorderLayout.NORTH);
		p.add(button(), BorderLayout.CENTER);

		return p;
	}

	private JPanel alarm() {
		JPanel p = new JPanel(new BorderLayout());

		p.setBackground(Color.WHITE);

		SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초"); // 날짜 표시 형태

		Calendar time = Calendar.getInstance(); // 날짜 가지고 오기

		String format_time = format.format(time.getTime()); // 시간 가지고 오기

		JLabel timelabel = new JLabel(format_time + "에 접속했습니다.");

		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, ""));
		p.add(label, BorderLayout.WEST); // label은 몇개의 사이트가 등록되었는지
		p.add(timelabel, BorderLayout.EAST);
		Font font = new Font("serial", Font.BOLD, 15); // label 폰트 변경하기
		label.setFont(font);
		timelabel.setFont(font);
		return p;

	}

	private JPanel info() { // 기본정보+추가정보
		JPanel p = new JPanel(new BorderLayout());

		p.add(basicInfo(), BorderLayout.NORTH);
		p.add(additionalInfo(), BorderLayout.CENTER);

		return p;
	}

	private JPanel basicInfo() { // 기본정보
		JPanel p = new JPanel(new GridLayout(4, 1));

		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "기본 정보"));
		p.setSize(200, 100);
		p.add(name());
		p.add(url());
		p.add(id());
		p.add(passwd());

		return p;
	}

	private JPanel name() { // 사이트명
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("사이트명"));
		p.add(name);

		return p;
	}

	private JPanel url() { // 주소
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("주소(URL) http:// "));
		p.add(url);

		return p;
	}

	private JPanel id() { // 아이디
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("아  이  디"));
		p.add(id);

		return p;
	}

	private JPanel passwd() { // 비밀번호
		JPanel p = new JPanel();

		p.setBackground(Color.YELLOW);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("비밀번호"));
		p.add(passwd);

		return p;
	}

	private JPanel additionalInfo() { // 추가정보
		JPanel p = new JPanel(new BorderLayout());
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "추가 정보"));
		p.setSize(200, 100);
		p.add(north(), BorderLayout.NORTH);
		p.add(memo(), BorderLayout.CENTER);

		return p;
	}

	private JPanel north() { // 메모제외 분류 + 선호도
		JPanel p = new JPanel(new GridLayout(2, 1));

		p.setBackground(Color.orange);
		p.add(category());
		p.add(prefer());

		return p;
	}

	private JPanel category() { // 분류
		JPanel p = new JPanel();

		p.setBackground(Color.orange);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("분        류"));

		p.add(categoryCombo);

		return p;
	}

	private JPanel prefer() { // 선호도
		JPanel p = new JPanel();

		p.setBackground(Color.orange);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("선     호     도"));

		p.add(preferCombo);

		return p;
	}

	private JPanel memo() { // 메모
		JPanel p = new JPanel();

		p.setBackground(Color.orange);
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("메         모"));
		p.add(new JScrollPane(textArea));

		return p;
	}

	private JPanel button() { // 버튼 2개
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton graphicButton = new JButton("그래픽보기");
	    graphicButton.setToolTipText("그래픽보기를 누르면 등록현황을 볼 수 있습니다.");
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

		rewriteButton.addActionListener(new ActionListener() { // 새로작성 버튼 누르면, 초기화되는 이벤트 리스너

			@Override
			public void actionPerformed(ActionEvent e) {
				name.setText("");
				url.setText("");
				id.setText("");
				passwd.setText("");
				textArea.setText("");

				inputButton.setText("입력(I)");
				categoryCombo.setSelectedIndex(0);
				preferCombo.setSelectedIndex(0);
			}

		});

		return p;
	}

	private JTabbedPane createTab() { // 탭 창
		JTabbedPane p = new JTabbedPane();

		p.addTab("사이트 목록", SList());
		p.addTab("등록현황(원)", RStatus());
		p.addTab("등록현황(막대)", RStatus2());

		return p;
	}

	private JPanel IDInfo() { // 계정정보
		JPanel p = new JPanel();

		checkbox = new JCheckBox("계정정보 보기");
		checkbox = new JCheckBox("계정정보 보기");
		checkbox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				BufferedWriter bfw;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					try {
						// 체크박스
						bfw = new BufferedWriter(new FileWriter("idcheckbox.txt", false)); // 덮어쓰기
						bfw.write("true_true"); // 체크되어 있으면 truetrue 반환
						bfw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("파일을 찾을 수 없습니다");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		JLabel iid = new JLabel("아이디");

		idfield = new JTextField(8);
		JLabel pw = new JLabel("비밀번호");
		pwfield = new JTextField(8);

		p.add(checkbox);
		p.add(iid);
		p.add(idfield);
		p.add(pw);
		p.add(pwfield);

		return p;
	}

	private JPanel DeleteBtn() { // 삭제버튼
		JPanel p = new JPanel();
		delete.setEnabled(false);
		p.add(delete);

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					number = number - 1; // 사이트 개수 -1
					label.setText(number + "개의 사이트가  등록되어있습니다.");
					// 리스트 삭제

					int rowIndex = table.getSelectedRow();

					// 선택 안하고 누를 경우 리턴값 -1
					if (result == -1)
						return;

					vv.remove(rowIndex);
					infotableModel.fireTableDataChanged();
					table.updateUI();
					if(table.getRowCount()==0) {
						delete.setEnabled(false);
					}
					// 데이터 지우기
				}

			}

		});
		return p;
	}

	private JPanel Tab_Bottom() { // 계정정보 + 삭제버튼
		JPanel p = new JPanel(new BorderLayout());

		p.add(IDInfo(), BorderLayout.WEST);
		p.add(DeleteBtn(), BorderLayout.EAST);

		return p;
	}

	private JPanel SList() { // 사이트 목록 탭
		JPanel p = new JPanel(new BorderLayout());

		p.add(SList_North(), BorderLayout.NORTH);
		p.add(SList_Center(), BorderLayout.CENTER);
		p.add(Tab_Bottom(), BorderLayout.SOUTH);

		return p;
	}

	private JPanel SList_North() {
		JPanel p = new JPanel(new BorderLayout());
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "검색/정렬"));

		p.add(createSearch(), BorderLayout.WEST);
		p.add(createSort(), BorderLayout.CENTER);

		return p;
	}

	private JPanel createSort() { // 정렬
		JPanel p = new JPanel(new FlowLayout());

		p.setBackground(Color.GREEN);
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "정렬"));
		p.setSize(200, 100);
		p.add(CategorizeCombo);
		p.add(SiteCombo);
		p.add(SortButton);
		SortButton.setToolTipText(" ! 정렬후 꼭 다시 기본버튼을 눌러주십시시오 !");
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
				if (b == "선호도") {
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
				if (b == "사이트 이름") {
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
				if (b == "사이트 주소") {
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
				table.setModel(new InfoTableModel(f)); // ss 는 검색 패널에서 콤보박스와 텍스트필드 필터링해서 모아둔 변수

				table.updateUI();
			}

		});

		SortDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				table.setModel(new InfoTableModel(vv));
				// 나영: infos 는 기존 정보를 다시 불러온다.

				table.updateUI();
				s.clear();
				f.clear();
				fa.clear();

			}

		});

		return p;
	}

	int q = 0;

	private JPanel createSearch() { // 검색
		JPanel p = new JPanel(new FlowLayout());
		p.setBackground(Color.GREEN);
		Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
		p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "검색"));
		p.add(SearchCombo);
		p.add(new JLabel("필터: "));
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

	private JPanel SList_Center() { // 나영
		JPanel p = new JPanel(new BorderLayout());

		infotableModel = new InfoTableModel(vv);
		table = new JTable(infotableModel);
		table.setAutoCreateRowSorter(true);
		// 나영: 테이블 헤더부분을 누르면 자동으로 정렬기능 추가

		table.setRowHeight(30);
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				idfield.setEditable(true);
				pwfield.setEditable(true);
				delete.setEnabled(true);
				
				tablerow = table.getSelectedRow();
				inputButton.setText("수정(E)"); // 수정 버튼으로 변경

				table = (JTable) e.getComponent();
				infotableModel = (InfoTableModel) table.getModel();

				checkbox.addItemListener(new ItemListener() { // 계정정보보기 체크박스에서

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) { // 계정정보보기 체크박스 선택
							idfield.setText(vv.get(tablerow).geti()); // 표에 입력한 id를 텍스트필드에 표시
							pwfield.setText(vv.get(tablerow).getp()); // 표에 입력한 pw를 텍스트필드에 표시
						} else { // 계정정보보기 체크박스 미선택
							idfield.setText(""); // 아무것도 안 보임
							pwfield.setText("");
						}
					}

				});
				// 표에 입력한 url,name을 텍스트필드에 표시
				url.setText(vv.get(tablerow).getu());
				name.setText(vv.get(tablerow).getname());

				// 표에 입력한 memo,combobox들 텍스트필드에 표시
				textArea.setText(vv.get(tablerow).getm());
				categoryCombo.setSelectedItem(vv.get(tablerow).getCategory());
				preferCombo.setSelectedItem(vv.get(tablerow).getPrefer());
			}
		});
		p.add(new JScrollPane(table), BorderLayout.CENTER);

		return p;
	}

	private JPanel RStatus() { // 등록현황 탭
		panel = new JPanel(new BorderLayout()); // 비어있는 등록현황 패널만 만들어 놓음
		return panel;
	}

	class Slice { // 비율과 색이 들어있는 slice 클래스
		double value;
		Color color;

		public Slice(double value, Color color) {
			this.value = value;
			this.color = color;
		}
	}

	class PieChart extends JComponent { // 등록현황 탭 - 원
		// hashmap 개수만큼 slice를 생성
		// slice마다 다른 색, 다른 비율
		// 비율하는거 추가해야함
		Slice[] slices = new Slice[categoryCombo.getItemCount()]; // 분류 크기만큼 slices 만들기

		int count = 0; // for문을 돌려서 표의 분류를 하나씩 돌려서 categorylist 별로 수를 하나씩 더한다.
		String value;
		double[] ccategoryname = new double[categoryCombo.getItemCount()]; // 비율을 계산하기 위해 double 사용
		int total;

		public void paint(Graphics g) { // 그리기
			for (int i = 0; i < categoryCombo.getItemCount(); i++) {
				ccategoryname[i] = 0; // 분류에 각각 0을 집어넣는다
			}
			for (int i = 0; i < table.getRowCount(); i++) {
				value = (String) table.getValueAt(i, 0); //
				for (int j = 0; j < categoryCombo.getItemCount(); j++) {
					String categoryname = categoryCombo.getItemAt(j);
					if (value.equals(categoryname) == true) { // 표에 분류 카테고리가 있으면
						(ccategoryname[j]) += 1; // 분류를 세는 카운트 수를 더한다
					}

				}
			}

			for (int i = 0; i < ccategoryname.length; i++) {
				total += ccategoryname[i]; // 카운트들을 모두 더한다
			}

			for (int i = 0; i < ccategoryname.length; i++) {
				if (ccategoryname[i] != 0) {
					ccategoryname[i] = (ccategoryname[i] * 100) / total; // 비율을 ccategoryname[i]에 저장한다.
				}
			}

			for (int x = 0; x < ccategoryname.length; x++) { // categoryList로 바꿈
				Color color = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0),
						(int) (Math.random() * 255.0)); // 색을 랜덤으로 만든다

				slices[x] = new Slice(ccategoryname[x], color); // slice에 비율과 색을 지정한다

			}
			drawPie((Graphics2D) g, getBounds(), slices);
		}

		void drawPie(Graphics2D g, Rectangle area, Slice[] slices) {
			double total = 0.0D;
			for (int i = 0; i < slices.length; i++) {
				total += slices[i].value; // 슬라이스 값을 다 더한다
			}
			double curValue = 0.0D;
			int startAngle = 0;
			for (int i = 0; i < slices.length; i++) {
				startAngle = (int) (curValue * 360 / total); // 전체 360도에서 시작하는 각도
				int arcAngle = (int) (slices[i].value * 360 / total); // 그리는 각도
				g.setColor(slices[i].color);
				g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);
				curValue += slices[i].value;
			}
		}
	}

	private JPanel RStatus2() { // 등록현황 탭 - 막대그래프
		panel2 = new JPanel(new BorderLayout());
		return panel2;
	}

	class DrawingPanel extends JPanel {
		public void paint(Graphics g) {
			int total = 0;
			String value;
			g.clearRect(0, 0, getWidth(), getHeight());
			g.drawLine(50, 250, 450, 250); // 가로 선
			for (int cnt = 1; cnt < 11; cnt++) { // 수평으로 선을 그어놓는다 대략 10단위
				g.drawLine(50, 250 - 20 * cnt, 450, 250 - 20 * cnt);
			}

			g.drawLine(50, 20, 50, 250); // 세로 선

			for (int i = 0; i < categoryCombo.getItemCount(); i++) {
				g.drawString(categoryCombo.getItemAt(i), 70 * (i + 1), 270); // 분류종류 글씨 나타내기
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
					g.fillRect((i + 1) * 70 + 10, 250 - ccategoryname[i] * 2, 10, ccategoryname[i] * 2); // 막대 그리기
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

					JComboBox cb = (JComboBox) e.getSource(); // 콤보박스 알아내기
					String category = (String) cb.getSelectedItem();// 선택된 아이템의 인덱스

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

					JComboBox cb = (JComboBox) e.getSource(); // 콤보박스 알아내기
					String category = (String) cb.getSelectedItem();// 선택된 아이템의 인덱스

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
			String[] s = { "분류", "선호도", "사이트 이름", "사이트 주소" };

			mColumnNames = new Vector<String>(s.length);

			for (int i = 0; i < s.length; i++)
				mColumnNames.addElement(s[i]);

			data = vv;

		}

		public void removeAllRow() {
			data.clear();
		}

		@Override
		public int getColumnCount() { // 열을 어떻게 알아낼까

			return mColumnNames.size();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			// data는 해쉬셋이기때문에 리스트로 변환해준다.
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
		private JLabel pw = new JLabel("비밀번호 ");
		private JTextField pwt = new JTextField(10);

		private JButton loginButton = new JButton("로그인");
		private JButton cancelButton = new JButton("종료");

		ArrayList<Integer> id2;
		UserDialog i2 = new UserDialog(null);

		public LoginDialog(JFrame frame, String title) {
			super(frame, title, true);
			add(buildlogin());

			setLocation(400, 400);
			setSize(250, 180);
			// x누르면 종료안돼
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
			JLabel ex = new JLabel("초기 비밀번호는 1217입니다.");
			p.setBackground(Color.WHITE);
			p.add(ex);
			return p;
		}

		private JPanel buildlogin() {
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.WHITE);
			p.setBorder(new TitledBorder(new LineBorder(Color.gray), "안녕하세요?"));
			p.add(Explain(), BorderLayout.SOUTH);
			p.add(LoginDialog_Button(), BorderLayout.CENTER);
			p.add(LoginDialog_Input(), BorderLayout.NORTH);

			return p;
		}

		ActionListener login_cancel_action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = pwt.getText();

				// UserDialog에서 ArrayList 비밀번호 값들을 가져온다. !!!!
				// 입력했던 것과 ArrayList가 가지고 있는 것을 비교한다.
				// 있던 것을 입력했을 때 false가 되게 한다.
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

//보경: 사용자 다이얼로그
	public class UserDialog extends JDialog {

		JTextField editText = new JTextField(10);
		JTextField userid = new JTextField(10);
		JButton deleteButton = new JButton("사용자 삭제");
		JButton addButton = new JButton("사용자 추가");

		JList<String> list = new JList<String>(new DefaultListModel<String>());
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		String firstid = "1217";// 초기 비밀번호

		// 보경: 파일에서 읽고 쓰기
		// 보경: ObjectOutputStream, FileOutputStream --> 파일에 저장
		// 보경: FileInputStream, ObjectInputStream --> ArrayList<Integer>파일에서 읽음
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		FileInputStream fin = null;
		ObjectInputStream oin = null;

		public UserDialog(JFrame f) {

			super(f, "사용자 관리", true);

			buildGUI();

			this.setSize(500, 300);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			try {

				// 보경: 읽기
				fin = new FileInputStream("LoginList.txt");
				oin = new ObjectInputStream(fin);

				// 보경: 읽은것을 배열로 다시 형변환해서 idList에 넣기
				idList = (ArrayList) oin.readObject();

				// 보경: 모델에 따로 추가하기
				// 보경: idList의 size만큼 반복
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
			JLabel ex = new JLabel("비밀번호는 4자리 숫자로 이루어집니다.\n텍스트창에 번호를 입력하고 삭제버튼을 누르면 삭제됩니다.");
			p.add(ex);
			return p;
		}

		private JPanel content() {

			JPanel p = new JPanel(new GridLayout(1, 1));

			Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
			p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "등록항목"));

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

					// 보경: 아이터레이터 이용
					Iterator<Integer> it = idList.iterator();
					while (it.hasNext()) {
						Integer s1 = it.next();
						// 보경: s1이 userid와 같을때
						if (s1 == Integer.parseInt(userid.getText()))
							it.remove();

					}
					model.removeElement(s);
					try {

						// 보경: LoginList에 저장하기
						fout = new FileOutputStream("LoginList.txt", true);
						oout = new ObjectOutputStream(fout);
						// 보경: 쓰기
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

					// 보경: Integer타입이니까 형변환해서 idList에 넣어주기
					idList.add(Integer.parseInt(s));
					model.addElement(s);
					try {
						// 보경: FileOutputStream으로 파일 생성하기
						fout = new FileOutputStream("LoginList.txt");
						oout = new ObjectOutputStream(fout);
						// 보경: 쓰기
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
			super(f, "정보", true);

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

		private JPanel EditPanel() { // 편집 내용

			JPanel p = new JPanel(new BorderLayout());

			addButton = new JButton("추가(A)");
			addButton.setToolTipText("추가 후 추가완료 버튼을 누르세요!");
			addButton.setMnemonic('A');
			deleteButton = new JButton("삭제(D)");
			deleteButton.setToolTipText("삭제 후 삭제완료 버튼을 누르세요!");
			deleteButton.setMnemonic('D');

			Border GrayBorder = BorderFactory.createLineBorder(Color.GRAY);
			p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "편집 내용"));

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
			p.add(new JLabel("항목 이름"));
			p.add(editText);
			return p;
		}

		private JPanel EditPanelButton() {
			JPanel p = new JPanel(new FlowLayout());
			p.add(addButton);
			p.add(deleteButton);
			return p;
		}

		private JPanel EditButton() { // 신규 완료 버튼
			JPanel p = new JPanel(new FlowLayout());

			newButton = new JButton("신규(N)");
			newButton.setMnemonic('N');
			inputcompleteButton = new JButton("추가완료(P)");
			inputcompleteButton.setMnemonic('P');
			deletecompleteButton = new JButton("삭제완료(C)");
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
			p.setBorder(BorderFactory.createTitledBorder(GrayBorder, "등록항목"));

			p.add(new JScrollPane(list));

			return p;
		}

	}

}