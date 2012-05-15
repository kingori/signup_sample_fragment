package kr.pe.kingori.sample.signup;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends FragmentActivity {
	static String DATA_FRAGMENT_TAG = "data";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (savedInstanceState == null) {
			// Do first time initialization -- add initial fragment.
			Fragment userDataFragment;
			{
				userDataFragment = new UserDataFragment();
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.add(userDataFragment, DATA_FRAGMENT_TAG);
				ft.commit();
			}
			Fragment newFragment = new SignupStep1Fragment();
			getSupportFragmentManager().beginTransaction().add(R.id.singup_main, newFragment).commit();
		}
	}

	public void gotoNext(int currentStep) {
		switch (currentStep) {
		case 1:
			showNextFragment(new SignupStep2Fragment());
			break;
		case 2:
			showNextFragment(new SignupStep3Fragment());
			break;
		case 3:
			complete();
			break;
		}
	}

	private void showNextFragment(SingupFragment nextFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.singup_main, nextFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	private void complete() {
		startActivity(new Intent(getApplicationContext(), ResultActivity.class).putExtra("data",
				((UserDataFragment) getSupportFragmentManager().findFragmentByTag(DATA_FRAGMENT_TAG)).getUserData()));
		finish();
		Log.d("test", "finish");
	}

	/**
	 * 가입 정보를 담은 fragment. activity lifecycle과 무관하게 유지되어야 함.
	 * 
	 * @author kingori
	 * 
	 */
	public static class UserDataFragment extends Fragment {
		private Map<String, String> userData = null;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
			userData = new HashMap<String, String>();
		}

		public void setValue(String key, String value) {
			userData.put(key, value);
		}

		public HashMap<String, String> getUserData() {
			return new HashMap<String, String>(userData);
		}
	}

	/**
	 * 첫 단계
	 * 
	 * @author kingori
	 * 
	 */
	public static class SignupStep1Fragment extends SingupFragment implements OnClickListener {
		EditText etName;
		EditText etSex;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.signup_step_1, container, false);
			root.findViewById(R.id.btn_next).setOnClickListener(this);
			etName = (EditText) root.findViewById(R.id.et_name);
			etSex = (EditText) root.findViewById(R.id.et_sex);
			etSex.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						next();
						return true;
					}
					return false;
				}
			});
			return root;
		}

		@Override
		public void onResume() {
			super.onResume();
			etName.requestFocus();
			showKeyboard(getActivity(), etName);
		}

		@Override
		protected void saveData() {
			userFragment.setValue("name", etName.getText().toString());
			userFragment.setValue("sex", etSex.getText().toString());
		}

		@Override
		public void onClick(View v) {
			next();
		}

		@Override
		void loadData() {
			etName.setText(userFragment.userData.get("name"));
			etSex.setText(userFragment.userData.get("sex"));
		}

		@Override
		int getCurrentStep() {
			return 1;
		}
	}

	/**
	 * 둘째 단계
	 * 
	 * @author kingori
	 * 
	 */
	public static class SignupStep2Fragment extends SingupFragment implements OnClickListener {

		EditText etHobby;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.signup_step_2, container, false);
			root.findViewById(R.id.btn_next).setOnClickListener(this);
			etHobby = (EditText) root.findViewById(R.id.et_hobby);
			etHobby.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						next();
						return true;
					}
					return false;
				}
			});

			return root;
		}

		@Override
		void loadData() {
			etHobby.setText(userFragment.userData.get("hobby"));
		}

		@Override
		public void onResume() {
			super.onResume();
			etHobby.requestFocus();
			showKeyboard(getActivity(), etHobby);
		}

		@Override
		protected void saveData() {
			userFragment.setValue("hobby", etHobby.getText().toString());
		}

		@Override
		public void onClick(View v) {
			next();
		}

		@Override
		int getCurrentStep() {
			return 2;
		}

	}

	/**
	 * 셋째(마지막) 단계
	 * 
	 * @author kingori
	 * 
	 */
	public static class SignupStep3Fragment extends SingupFragment implements OnClickListener {

		EditText etAddr;
		EditText etTel;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.signup_step_3, container, false);
			root.findViewById(R.id.btn_next).setOnClickListener(this);
			etAddr = (EditText) root.findViewById(R.id.et_addr);
			etTel = (EditText) root.findViewById(R.id.et_tel);
			etTel.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						next();
						return true;
					}
					return false;
				}
			});

			return root;
		}

		@Override
		void loadData() {
			etAddr.setText(userFragment.userData.get("addr"));
			etTel.setText(userFragment.userData.get("tel"));
		}

		@Override
		public void onResume() {
			super.onResume();
			etAddr.requestFocus();
			showKeyboard(getActivity(), etAddr);
		}

		@Override
		protected void saveData() {
			userFragment.setValue("addr", etAddr.getText().toString());
			userFragment.setValue("tel", etTel.getText().toString());
		}

		@Override
		public void onClick(View v) {
			next();
		}

		@Override
		int getCurrentStep() {
			return 3;
		}
	}

	/**
	 * 가입화면 base class
	 * 
	 * @author kingori
	 * 
	 */
	public static abstract class SingupFragment extends Fragment {
		UserDataFragment userFragment;

		/**
		 * 사용자 정보에 접근할 수 있도록 userFragment 설정. userFragment는 activity가 create된 후에
		 * 접근 가능하므로 onActivityCreate 에서 작업함.
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			FragmentManager fm = getFragmentManager();
			userFragment = (UserDataFragment) fm.findFragmentByTag(DATA_FRAGMENT_TAG);
			loadData();
		}

		/**
		 * 입력 중간에 백 누를 경우에도 값을 저장해야 함. 다만 activity 종료로 detach 되는 경우는 제외.
		 */
		@Override
		public void onDetach() {
			if (!getActivity().isFinishing()) {
				Log.d("test", "detach");
				saveData();
			}
			super.onDetach();
		}

		/**
		 * 기존 데이터 불러오기
		 */
		abstract void loadData();

		/**
		 * 입력한 데이터 저장
		 */
		abstract void saveData();

		/**
		 * 현재 단계 알아내기
		 * 
		 * @return
		 */
		abstract int getCurrentStep();

		/**
		 * 다음 화면으로 진행하기
		 */
		protected void next() {
			// 다음 화면으로 가기 전에 일단 저장
			saveData();
			((MainActivity) getActivity()).gotoNext(getCurrentStep());
		}
	}

	/**
	 * 소프트키보드 표출 : 근데 잘 안됨...
	 * 
	 * @param ctx
	 * @param view
	 */
	public static void showKeyboard(Context ctx, View view) {
		try {
			InputMethodManager mgr = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		} catch (Throwable e) {
			Log.w("sample", e);
		}
	}
}
