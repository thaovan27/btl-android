package vn.edu.poly.assignment.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import vn.edu.poly.assignment.DAO.DAO_AC;
import vn.edu.poly.assignment.DTO.DTOAC;
import vn.edu.poly.assignment.R;


public class SinginFragment extends Fragment {


    private TextInputLayout etUserAdd;
    private TextInputLayout etPassAdd;
    private TextInputLayout etRepassAdd;


    private Button btnSingin;
    private Button btnBack;

    DAO_AC daoAc;
    ArrayList<DTOAC> list_ac;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dk, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daoAc = new DAO_AC(getContext());
        daoAc.opend();
        list_ac = daoAc.getALLAC();
        DTOAC obj = new DTOAC();
        btnSingin = (Button) view.findViewById(R.id.btn_singin);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        etUserAdd = view.findViewById(R.id.et_user_add);
        etPassAdd = view.findViewById(R.id.et_pass_add);
        etRepassAdd = view.findViewById(R.id.et_repass_add);
        btnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPasswordValid = checkpass();
                if (!isPasswordValid) {
                    return; // Dừng việc thực hiện tiếp theo nếu mật khẩu không hợp lệ
                }

                if (etUserAdd.getEditText().getText().toString().equals("")) {
                    etUserAdd.setError("Tên tài khoản không được để trống");
                    return; // Dừng việc thực hiện tiếp theo nếu tên tài khoản không hợp lệ
                }

                // Kiểm tra xem tên tài khoản đã tồn tại chưa
                String username = etUserAdd.getEditText().getText().toString();
                for (DTOAC ac : list_ac) {
                    if (ac.getUserName().equals(username)) {
                        etUserAdd.setError("Tài khoản đã tồn tại");
                        return; // Dừng việc thực hiện tiếp theo nếu tên tài khoản đã tồn tại
                    }
                }

                // Thêm tài khoản nếu không có lỗi
                DTOAC obj = new DTOAC();
                obj.setUserName(username);
                obj.setPassWord(etPassAdd.getEditText().getText().toString());
                obj.setRePass(etRepassAdd.getEditText().getText().toString());
                long res = daoAc.insert_ac(obj);
                if (res > 0) {
                    list_ac.clear();
                    list_ac.addAll(daoAc.getALLAC());
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_content1, new LoginFragment());
                    transaction.commit();
                    Toast.makeText(getContext(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    // Bạn có thể thực hiện thêm hành động ở đây nếu cần
                } else {
                    Toast.makeText(getContext(), "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    // Bạn có thể thực hiện xử lý lỗi khác ở đây nếu cần
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_content1, new LoginFragment());
                transaction.commit();
            }
        });

    }

    public boolean checkpass() {
        boolean a = true;
        if (etPassAdd.getEditText().getText().toString().trim().equals("")) {
            etPassAdd.setError("Mật khẩu không được để trống");
            a = false;
        } else {
            etPassAdd.setError("");
            a = true;
        }
        if (!etRepassAdd.getEditText().getText().toString().equals(etPassAdd.getEditText().getText().toString())) {
            etRepassAdd.setError("Không đúng mật khẩu");
            a = false;
        } else if (etRepassAdd.getEditText().getText().toString().trim().equals("")) {
            etRepassAdd.setError("Nhập lại mật khẩu không được để trống");
            a = false;
        } else {
            etRepassAdd.setError("");
            a = true;
        }
        return a;
    }
}