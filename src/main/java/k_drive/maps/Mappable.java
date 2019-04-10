package k_drive.maps;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

public interface Mappable {
	// 테이블 전체 조회
	@Select("select * from table(f_k_list(#{table_num}))")	
	public List<HashMap<String, String>> getKdriveList(int table_num);
	// 복사후 작업결과 리턴
	@Select("select * from table(f_copy_n_select(#{source}, #{target}, #{table}))")	
	public List<HashMap<String, String>> getKdriveCopy(@Param("source") int source, 
			@Param("target") int target, @Param("table") int table);
	// 파일 및 폴더 이동
	@Update(value = "{call p_move(#{source}, #{target}, #{table})}")
	@Options(statementType = StatementType.CALLABLE)
	public void kdriveMove(@Param("source") int source, @Param("target") int target, 
			@Param("table") int table);
	// 특정 폴더 조회
	@Select("select * from table(f_k_select(#{num}, #{table}))")	
	public List<HashMap<String, String>> getKdriveSelect(@Param("num") int num, 
			@Param("table") int table);
	// 폴더 생성후 순번 리턴
	@Select("select f_md_select(#{num}, #{name}, #{table}) s_num from dual")	
	public List<HashMap<String, String>> getKdriveMd(@Param("num") int num, 
			@Param("name") String name, @Param("table") int table);
	// 폴더 삭제
	@Update(value = "{call p_del_all(#{source}, #{table})}")
	@Options(statementType = StatementType.CALLABLE)
	public void kdriveDel(@Param("source") int source, @Param("table") int table);
	@Select("select * from table(f_upload_folder_n_select(#{sequence_number}, #{table}))")
	// 폴더 업로드 후 작업 폴더 리스트 리턴
	public List<HashMap<String, String>> 
		getKdriveUpFolder(@Param("sequence_number") int sequence_number, @Param("table") int table);			
	@Insert(value = "{call p_mf (#{target, mode=IN, jdbcType=INTEGER}, "
			+ "#{name, mode=IN, jdbcType=NVARCHAR}, "
			+ "#{oname, mode=IN, jdbcType=NVARCHAR}, "
			+ "#{table, mode=IN, jdbcType=INTEGER})}")
	public void makeFile(@Param("target") int target, @Param("name") String name, 
			@Param("oname") String oname, @Param("table") int table);	
	// 회원 아이디 조회
	@Select("select * from mb_jsp where m_userid = #{id}")
	public List<HashMap<String, String>> idCheck(@Param("id") String id);
	
	@Select("select f_md_select(#{target}, #{name}, #{table}) s_num from dual")
	public List<HashMap<String, String>> makeFolder(@Param("target") int target, 
			@Param("name") String name, @Param("table") int table);
}
