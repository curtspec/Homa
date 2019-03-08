package com.curtspec2018.homa.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Build;
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

import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.CurrentListAdapter;
import com.curtspec2018.homa.databinding.FragAccountCurrentBinding;
import com.curtspec2018.homa.vo.Account;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.MonthAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CurrentFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    LayoutInflater inflater;
    ListView listSI;
    ListView listFI;
    ListView listSE;
    ListView listFE;
    TextView tvSI, tvFI, tvSE, tvFE, tvRent, tvIncome, tvTotalIncome, tvTotalExpense, tvPure;

    CurrentListAdapter adapterSI;
    CurrentListAdapter adapterFI;
    CurrentListAdapter adapterSE;
    CurrentListAdapter adapterFE;

    ArrayList<Account> staticIncome;
    ArrayList<Account> floatIncome;
    ArrayList<Account> staticExpense;
    ArrayList<Account> floatExpense;

    MonthAccount currentMonth;
    Building currentBuilding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;

        //====================================== data setting ==================================================

        currentBuilding = G.getCurrentBuilding();
        String thisMonth = new SimpleDateFormat("yyyy.MM").format(new Date());
        if (currentBuilding != null) {
            currentMonth = currentBuilding.getCurrnetMonth();
            if (currentMonth == null){
                currentMonth = new MonthAccount(thisMonth, currentBuilding.getTotalRent(), new ArrayList<>(), new ArrayList<>());
            }
            if (!currentMonth.getWhen().equals(thisMonth)) {
                currentBuilding.getAccounts().add(0, currentMonth);
                currentMonth = new MonthAccount(thisMonth, currentBuilding.getTotalRent(), new ArrayList<>(), new ArrayList<>());
            }
        }else {
            currentMonth = new MonthAccount(thisMonth, 0, new ArrayList<>(), new ArrayList<>());
        }

        //=========================================================================================================

        staticIncome = currentMonth.getStaticIncomes();
        floatIncome = currentMonth.getFloatIncomes();
        staticExpense = currentMonth.getStaticExpense();
        floatExpense = currentMonth.getFloatExpense();

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
        tvRent = view.findViewById(R.id.tv_rent);
        tvIncome = view.findViewById(R.id.tv_income_etc);
        tvTotalIncome = view.findViewById(R.id.tv_income_total);
        tvTotalExpense = view.findViewById(R.id.tv_expense_total);
        tvPure = view.findViewById(R.id.tv_pure);

        synchToggleAndListview(view);
        resetValues();

        BottomNavigationView btmNavi = view.findViewById(R.id.bottom_navigation);
        btmNavi.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Building currentBuilding = G.getCurrentBuilding();
        if (currentBuilding != null){
            currentBuilding.setCurrnetMonth(currentMonth);
        }
    }

    private void resetValues(){
        int sumOfSI = 0;
        for (Account t : staticIncome) sumOfSI += t.getAmount();
        tvSI.setText(G.divisionThousand(sumOfSI));
        int sumOfFI = 0;
        for (Account t : floatIncome) sumOfFI += t.getAmount();
        tvFI.setText(G.divisionThousand(sumOfFI));
        int sumOfSE = 0;
        for (Account t : staticExpense) sumOfSE += t.getAmount();
        tvSE.setText(G.divisionThousand(sumOfSE));
        int sumOfFE = 0;
        for (Account t : floatExpense) sumOfFE += t.getAmount();
        tvFE.setText(G.divisionThousand(sumOfFE));
        tvIncome.setText(G.divisionThousand(sumOfSI + sumOfFI));
        long totalIncome = sumOfSI + sumOfFI + currentMonth.getRent();
        tvTotalIncome.setText(G.divisionThousand(totalIncome));
        tvTotalExpense.setText(G.divisionThousand(sumOfSE + sumOfFE));
        tvPure.setText(G.divisionThousand(totalIncome - (sumOfSE + sumOfFE)));
        tvRent.setText(G.divisionThousand(currentMonth.getRent()));
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
                                    if (position == 0) return true;
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
                        resetValues();
                        return true;
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
                resetValues();
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
                        resetValues();
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
                resetValues();
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
        }
        return true;
    }
}
