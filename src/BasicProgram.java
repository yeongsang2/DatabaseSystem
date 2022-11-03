import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.StringTokenizer;

import static java.lang.System.clearProperty;
import static java.lang.System.exit;

public class BasicProgram {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; //드라이버

    private static final String DB_URL = "jdbc:mysql://192.168.47.3:4567/madang"; //접속할 DB 서버
    private static final String USER_NAME = "yeongsang2"; //DB에 접속할 사용자 이름을 상수로 정의
    private static final String PASSWORD = "041102"; //사용자의 비밀번호를 상수로 정의
    private static Connection con;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static StringTokenizer st;
    public BasicProgram(){ //db연결

        try{
            Class.forName(JDBC_DRIVER);
            con=DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        }catch(Exception e){ System.out.println(e);}

    }
    public void start() throws IOException, SQLException {

        while(true){

            System.out.println("===============================");
            System.out.println("============0번:도서 종료========");
            System.out.println("============1번:도서 삽입========");
            System.out.println("============2번:도서 삭제========");
            System.out.println("============3번:도서 검색========");
            System.out.println("===============================");

            int opt;
            switch (opt = Integer.parseInt(br.readLine())) {
                case 0: opt = 0;{ //종료
                    con.close();
                    exit(0);
                    break;
                }
                case 1: opt = 1;{ //삽입
                    insert();
                    break;
                }
                case 2: opt = 2;{ //삭제
                    delete();
                    break;

                } case 3: opt =3;{ //검색
                    search();
                    break;
                }
            }
        }

    }
    // 도서 삽입
    public void insert(){

        try{

            ResultSet rs = con.createStatement().executeQuery("select bookid from Book order by bookid desc");
            //다음 bookid 조회
            rs.next();
            Integer nextBookId = rs.getInt(1)+1;
            // 도서 정보 입력
            System.out.println("도서 이름, 출판사, 가격을 입려해주세요");

            st = new StringTokenizer(br.readLine());
            String bookName= st.nextToken();
            String publisher= st.nextToken();
            String price= st.nextToken();

            String insertQuery = "insert into Book(bookid, bookname, publisher, price) values(?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setString(1, nextBookId.toString());
            pstmt.setString(2,bookName);
            pstmt.setString(3,publisher);
            pstmt.setString(4,price);
            pstmt.executeUpdate();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    // 도서 삭제
    public void delete(){
        try{
            System.out.println("삭제할 도서이름을 입력해주세요");
            String deleteBookName = br.readLine();
            PreparedStatement pstmt = con.prepareStatement("delete from Book where bookName = ?");
            pstmt.setString(
                    1,deleteBookName);
            pstmt.execute();

        }catch (Exception e){
            System.out.println(e);
        }
    }
    // 도서 검색
    public void search(){

        try{
            System.out.println("검색할 도서이름을 입력해주세요");
            String searchBookName = br.readLine();

            PreparedStatement pstmt = con.prepareStatement("select * from Book where bookname like ?");
            pstmt.setString(1,searchBookName);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){

                System.out.println("도서이름 : " + rs.getString(2));
                System.out.println("출판사 : " + rs.getString(3));
                System.out.println("가격 : " + rs.getString(4));
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }



    public static void main(String[] args) throws IOException, SQLException {

        BasicProgram basicProgram = new BasicProgram();
        basicProgram.start();

    }
}
