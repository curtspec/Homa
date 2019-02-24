package com.curtspec2018.homa.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.CurrentListAdapter;
import com.curtspec2018.homa.databinding.FragAccountCurrentBinding;
import com.curtspec2018.homa.vo.Account;

import java.util.ArrayList;

public class CurrentFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    FragAccountCurrentBinding b;
    LayoutInflater inflater;
    ListView listSI;
    ListView listFI;
    ListView listSE;
    ListView listFE;
    TextView tvSI, tvFI, tvSE, tvFE;

    CurrentListAdapter adapterSI;
    CurrentListAdapter adapterFI;
    CurrentListAdapter adapterSE;
    CurrentListAdapter adapterFE;

    ArrayList<Account> staticIncome = new ArrayList<>();
    ArrayList<Account> floatIncome = new ArrayList<>();
    ArrayList<Account> staticExpense = new ArrayList<>();
    ArrayList<Account> floatExpense = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.frag_account_current, container, false);
        this.inflater = inflater;
        //=============================================  test datas  =======================================
        staticIncome.add(new Account("이자수익", 30000));
        staticIncome.add(new Account("이자수익", 30000));
        staticExpense.add(new Account("청소업체", 400000));

        adapterSI = new CurrentListAdapter(inflater, staticIncome);
        adapterFI = new CurrentListAdapter(inflater, floatIncome);
        adapterSE = new CurrentListAdapter(inflater, staticExpense);
        adapterFE = new CurrentListAdapter(inflater, floatExpense);

        return inflater.inflate(R.layout.frag_account_current, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listSI = view.findViewById(R.id.listview_static_income);
        listFI = view.findViewById(R.id.listview_float_income);
        listSE = view.findViewById(R.id.listview_static_expense);
        listFE = view.findViewById(R.id.listview_float_expense);
        listSI.setAdapter(adapterSI);
        listFI.setAdapter(adapterFI);
        listSE.setAdapter(adapterSE);
        listFE.setAdapter(adapterFE);
        setListviewHeight(listSI, adapterSI);
        setListviewHeight(listFI, adapterFI);
        setListviewHeight(listSE, adapterSE);
        setListviewHeight(listFE, adapterFE);

        tvSI = view.findViewById(R.id.tv_static_income);
        tvFI = view.findViewById(R.id.tv_float_income);
        tvSE = view.findViewById(R.id.tv_static_expense);
        tvFE = view.findViewById(R.id.tv_float_expense);

        synchToggleAndListview(view);
        resetValues();

        BottomNavigationView btmNavi = view.findViewById(R.id.bottom_navigation);
        btmNavi.setOnNavigationItemSelectedListener(this);
    }

    private void resetValues(){
        int amount = 0;
        for (Account t : staticIncome) amount += t.getAmount();
        tvSI.setText(amount+"");
        amount = 0;
        for (Account t : floatIncome) amount += t.getAmount();
        tvFI.setText(amount+"");
        amount = 0;
        for (Account t : staticExpense) amount += t.getAmount();
        tvSE.setText(amount+"");
        amount = 0;
        for (Account t : floatExpense) amount += t.getAmount();
        tvFE.setText(amount+"");

        //TODO :
    }

    private void setListviewHeight(ListView list, CurrentListAdapter adapter){
        ViewGroup.LayoutParams params = list.getLayoutParams();
        int h = (int) (adapter.getCount() * dpToPx(30, getContext()));
        params.height = h;
        list.setLayoutParams(params);
    }

    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private void synchToggleAndListview( View v ){
        ToggleButton toggleSI =v.findViewById(R.id.toggle_static_income);
        toggleSI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listSI.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        ToggleButton toggleFI =v.findViewById(R.id.toggle_float_income);
        toggleFI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listFI.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        ToggleButton toggleSE =v.findViewById(R.id.toggle_static_expense);
        toggleSE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listSE.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        ToggleButton toggleFE =v.findViewById(R.id.toggle_float_expense);
        toggleFE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listFE.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void showIncomeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = inflater.inflate(R.layout.dialog_current, null);

        final ListView listView = v.findViewById(R.id.list);
        final RadioGroup rg = v.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_static) {
                    listView.setAdapter(adapterSI);
                }
                else listView.setAdapter(adapterFI);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.house_edit);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_delete:
                                if (rg.getCheckedRadioButtonId() == R.id.rb_static){
                                    staticIncome.remove(position);
                                    adapterSI.notifyDataSetChanged();
                                    setListviewHeight(listSI, adapterSI);
                                }else {
                                    floatIncome.remove(position);
                                    adapterFI.notifyDataSetChanged();
                                    setListviewHeight(listFI, adapterFI);
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        RadioButton rbStatic = v. findViewById(R.id.rb_static);
        rbStatic.setChecked(true);
        final EditText editTitle = v.findViewById(R.id.edit_title);
        final EditText editAmount = v.findViewById(R.id.edit_amount);
        ImageView btnAdd = v.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String amount = editAmount.getText().toString();
                if (title.equals("") || amount.equals("")) return;
                if(rg.getCheckedRadioButtonId() == R.id.rb_static){
                    staticIncome.add(0,new Account(title, Integer.parseInt(amount)));
                    adapterSI.notifyDataSetChanged();
                    setListviewHeight(listSI, adapterSI);
                }else {
                    floatIncome.add(0,new Account(title, Integer.parseInt(amount)));
                    adapterFI.notifyDataSetChanged();
                    setListviewHeight(listFI, adapterFI);
                }
            }
        });
        builder.setView(v);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listSI.invalidate();
                listFI.invalidate();
            }
        });
        builder.show().setCanceledOnTouchOutside(false);
    }

    public void showExpenseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = inflater.inflate(R.layout.dialog_current, null);

        final ListView listView = v.findViewById(R.id.list);
        final RadioGroup rg = v.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_static) {
                    listView.setAdapter(adapterSE);
                }
                else listView.setAdapter(adapterFE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.house_edit);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_delete:
                                if (rg.getCheckedRadioButtonId() == R.id.rb_static){
                                    staticExpense.remove(position);
                                    adapterSE.notifyDataSetChanged();
                                    setListviewHeight(listSE, adapterSE);
                                }else {
                                    floatExpense.remove(position);
                                    adapterFE.notifyDataSetChanged();
                                    setListviewHeight(listFE, adapterFE);
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        RadioButton rbStatic = v. findViewById(R.id.rb_static);
        rbStatic.setChecked(true);
        final EditText editTitle = v.findViewById(R.id.edit_title);
        final EditText editAmount = v.findViewById(R.id.edit_amount);
        ImageView btnAdd = v.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String amount = editAmount.getText().toString();
                if (title.equals("") || amount.equals("")) return;
                if(rg.getCheckedRadioButtonId() == R.id.rb_static){
                    staticExpense.add(new Account(title, Integer.parseInt(amount)));
                    adapterSE.notifyDataSetChanged();
                    setListviewHeight(listSE, adapterSE);
                }else {
                    floatExpense.add(new Account(title, Integer.parseInt(amount)));
                    adapterFE.notifyDataSetChanged();
                    setListviewHeight(listFE, adapterFE);
                }
            }
        });
        builder.setView(v);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show().setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_add_income:
                showIncomeDialog();
                break;
            case R.id.menu_add_expense:
                showExpenseDialog();
                break;
            case R.id.menu_capture:

                break;
        }
        return false;
    }
}
