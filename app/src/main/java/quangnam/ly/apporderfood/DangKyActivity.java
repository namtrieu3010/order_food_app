package quangnam.ly.apporderfood;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import quangnam.ly.apporderfood.DAO.NhanVienDAO;
import quangnam.ly.apporderfood.DAO.QuyenDAO;
import quangnam.ly.apporderfood.DTO.NhanVienDTO;
import quangnam.ly.apporderfood.DTO.QuyenDTO;
import quangnam.ly.apporderfood.FragmentApp.DatePickerFragment;

import com.minhhung.apporderfood.R;

import java.util.ArrayList;
import java.util.List;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText edTenDangNhapDK, edMatKhauDK, edNgaySinhDK,edCMNDDK;
    Button btnDongYDK, btnThoatDK;
    RadioGroup rgGioiTinh;
    RadioButton rdNam,rdNu;
    TextView txtTieuDeDangKy;
    String sGioiTinh;
    Spinner spinQuyen;
    NhanVienDAO nhanVienDAO;
    QuyenDAO quyenDAO;
    int manv = 0;
    int landautien = 0;
    List<QuyenDTO> quyenDTOList;
    List<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);

        edTenDangNhapDK = (EditText) findViewById(R.id.edTenDangNhapDK);
        edMatKhauDK = (EditText) findViewById(R.id.edMatKhauDK);
        edNgaySinhDK = (EditText) findViewById(R.id.edNgaySinhDK);
        txtTieuDeDangKy = (TextView) findViewById(R.id.txtTieuDeDangKy);
        rdNam = (RadioButton) findViewById(R.id.rdNam);
        rdNu = (RadioButton) findViewById(R.id.rdNu);
        edCMNDDK = (EditText) findViewById(R.id.edCMNDDK);
        btnDongYDK = (Button) findViewById(R.id.btnDongYDK);
        btnThoatDK = (Button) findViewById(R.id.btnThoatDK);
        rgGioiTinh = (RadioGroup) findViewById(R.id.rgGioiTinh);
        spinQuyen = (Spinner) findViewById(R.id.spinQuyen);

        btnDongYDK.setOnClickListener(this);
        btnThoatDK.setOnClickListener(this);
        edNgaySinhDK.setOnFocusChangeListener(this);

        nhanVienDAO = new NhanVienDAO(this);
        quyenDAO = new QuyenDAO(this);

        quyenDTOList = quyenDAO.LayDanhSachQuyen();
        dataAdapter = new ArrayList<String>();

        for (int i=0; i<quyenDTOList.size();i++){
            String tenquyen = quyenDTOList.get(i).getTenQuyen();
            dataAdapter.add(tenquyen);
        }

        //quyenDAO.ThemQuyen("quản lý");  // Ung voi ma quyen la 1
        //quyenDAO.ThemQuyen("nhân viên"); //ung voi ma quyen la 2

        manv = getIntent().getIntExtra("manv",0);
        landautien = getIntent().getIntExtra("landautien",0);

        if(landautien == 0){        //khong phai admim
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataAdapter);
            spinQuyen.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{ //la admin
            spinQuyen.setVisibility(View.GONE);
        }


        if(manv != 0){
            txtTieuDeDangKy.setText(getResources().getString(R.string.capnhatnhanvien));
            NhanVienDTO nhanVienDTO = nhanVienDAO.LayDanhSachNhanVienTheoMa(manv);

            edTenDangNhapDK.setText(nhanVienDTO.getTENDN());
            edMatKhauDK.setText(nhanVienDTO.getMATKHAU());
            edNgaySinhDK.setText(nhanVienDTO.getNGAYSINH());
            edCMNDDK.setText(String.valueOf(nhanVienDTO.getCMND()));

            String gioitinh = nhanVienDTO.getGIOITINH();
            if(gioitinh.equals("Nam")){
                rdNam.setChecked(true);
            }else{
                rdNu.setChecked(true);
            }

            edNgaySinhDK.setText(nhanVienDTO.getNGAYSINH());
            edCMNDDK.setText(String.valueOf(nhanVienDTO.getCMND()));
        }

    }

    private void DongYThemNhanVien(){
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        switch (rgGioiTinh.getCheckedRadioButtonId()){
            case R.id.rdNam:
                sGioiTinh = "Nam";
                break;

            case R.id.rdNu:
                sGioiTinh = "Nữ";
                break;
        }
        String sNgaySinh = edNgaySinhDK.getText().toString();
        int iCMND;

        if(sTenDangNhap == null || sTenDangNhap.equals("") || sTenDangNhap.isEmpty() || sTenDangNhap.trim().isEmpty())
        {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.loinhaptendangnhap), Toast.LENGTH_LONG).show();
        }
        else if(sMatKhau == null || sMatKhau.equals("") || sMatKhau.isEmpty() || sMatKhau.trim().isEmpty())
        {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.loinhapmatkhau), Toast.LENGTH_LONG).show();
        }
        else if(edCMNDDK.getText().toString() == null || edCMNDDK.getText().toString().trim().isEmpty() || edCMNDDK.getText().toString().isEmpty() || edCMNDDK.getText().toString().equals(""))
        {
            Toast.makeText(DangKyActivity.this, getResources().getString(R.string.loinhapchungminhnhandan), Toast.LENGTH_LONG).show();
        }
        else
        {
            iCMND = Integer.parseInt(edCMNDDK.getText().toString());
            NhanVienDTO nhanVienDTO = new NhanVienDTO();
            nhanVienDTO.setTENDN(sTenDangNhap);
            nhanVienDTO.setMATKHAU(sMatKhau);
            nhanVienDTO.setCMND(iCMND);
            nhanVienDTO.setNGAYSINH(sNgaySinh);
            nhanVienDTO.setGIOITINH(sGioiTinh);
            if(landautien != 0){
                //gán mặc định quyền nhân viên là admin
                nhanVienDTO.setMAQUYEN(1);
            }else{
                //gán quyền bằng quyền mà admin chọn khi tạo nhân viên
                int vitri = spinQuyen.getSelectedItemPosition();
                int maquyen = quyenDTOList.get(vitri).getMaQuyen();
                nhanVienDTO.setMAQUYEN(maquyen);
            }

            long kiemtra = nhanVienDAO.ThemNhanVien(nhanVienDTO);
            if(kiemtra != 0){
                Toast.makeText(DangKyActivity.this,getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(DangKyActivity.this,getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SuaNhanVien(){
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        String sNgaySinh = edNgaySinhDK.getText().toString();
        int sCMND = Integer.parseInt(edCMNDDK.getText().toString());
        switch (rgGioiTinh.getCheckedRadioButtonId()){
            case R.id.rdNam:
                sGioiTinh = "Nam";
                break;

            case R.id.rdNu:
                sGioiTinh = "Nữ";
                break;
        }

        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        nhanVienDTO.setMANV(manv);
        nhanVienDTO.setTENDN(sTenDangNhap);
        nhanVienDTO.setMATKHAU(sMatKhau);
        nhanVienDTO.setCMND(sCMND);
        nhanVienDTO.setNGAYSINH(sNgaySinh);
        nhanVienDTO.setGIOITINH(sGioiTinh);

        boolean kiemtra = nhanVienDAO.SuaNhanVien(nhanVienDTO);
        if(kiemtra){
            Toast.makeText(DangKyActivity.this,getResources().getString(R.string.suathanhcong),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(DangKyActivity.this,getResources().getString(R.string.loi),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnDongYDK:
                if(manv != 0){
                    // Thực hiện code sữa nhân viên
                    SuaNhanVien();
                }else{
                    // Thực hiện thêm mới nhân viên
                   DongYThemNhanVien();
                }


                ;break;

            case R.id.btnThoatDK:
                finish();break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id){
            case R.id.edNgaySinhDK:
                if(hasFocus){
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(),"Ngày Sinh");
//                    datePickerFragment
                    //xuat popup ngaysinh
                }
                ;break;
        }
    }

}
