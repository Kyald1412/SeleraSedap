
package com.oxyexpress.restaurants.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.Cart;
import com.oxyexpress.restaurants.MainActivity;
import com.oxyexpress.restaurants.R;
import com.oxyexpress.restaurants.object.OfferObject;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.abtractclass.fragment.DBFragmentAdapter;
import com.ypyproductions.net.task.DBTask;
import com.ypyproductions.utils.ApplicationUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class FragmentBooking extends DBFragment {

	public static final String TAG = FragmentBooking.class.getSimpleName();

	private MainActivity mContext;
	private Button login;
	private ArrayList<Fragment> mListFragments = new ArrayList<Fragment>();

	Session session = null;
	EditText reciep;
	EditText sub;
	EditText msg;
	EditText msg1;
	static EditText msg2;
	EditText msg4,msg3;
	String rec, subject, textMessage, textMessage1, textMessage2, textMessage3, textMessage4, str;
	TextView tv4;

	@Override
	public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_booking, container, false);
	}

	@Override
	public void findView() {
		this.mContext = (MainActivity) getActivity();

		this.login = (Button) mRootView.findViewById(R.id.btn_submit);
		this.reciep = (EditText) mRootView.findViewById(R.id.et_to);
		this.sub = (EditText) mRootView.findViewById(R.id.et_sub);
		this.msg = (EditText) mRootView.findViewById(R.id.et_text);
		this.msg1 = (EditText) mRootView.findViewById(R.id.et_text1);
		this.msg2 = (EditText) mRootView.findViewById(R.id.et_text2);
		this.msg3 = (EditText) mRootView.findViewById(R.id.et_text3);
		this.msg4 = (EditText) mRootView.findViewById(R.id.et_text4);


		Typeface mTypefaceSignika = Typeface.createFromAsset(getContext().getAssets(), "fonts/Signika-Light.otf");
		this.login.setTypeface(mTypefaceSignika);
		this.reciep.setTypeface(mTypefaceSignika);
		this.sub.setTypeface(mTypefaceSignika);
		this.msg.setTypeface(mTypefaceSignika);
		this.msg1.setTypeface(mTypefaceSignika);
		this.msg2.setTypeface(mTypefaceSignika);
		this.msg3.setTypeface(mTypefaceSignika);
		this.msg4.setTypeface(mTypefaceSignika);

		this.login.setEnabled(false);
		this.login.setBackgroundColor(getResources().getColor(R.color.grey));


		this.msg2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment dialogFragment = new displayDateFragment();
				dialogFragment.show(getChildFragmentManager(), "DatePicEnker");
			}
		});



		this.msg.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});



		this.msg1.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});



		this.msg2.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});



		this.msg3.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});


		this.msg4.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});


		this.login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ApplicationUtils.isOnline(mContext)) {
					mContext.showToast(com.oxyexpress.restaurants.R.string.info_lose_internet);
				} else {

					String email = "hunginda@gmail.com";
					subject = sub.getText().toString();
					textMessage = msg.getText().toString();
					textMessage1 = msg1.getText().toString();
					textMessage2 = msg2.getText().toString();
					textMessage3 = msg3.getText().toString();
					textMessage4 = msg4.getText().toString();
					sendMail(email, subject, textMessage, textMessage1, textMessage2, textMessage3,textMessage4);

				}
			}
		});
	}


	private static final String judul = "Booking tempat selera sedap";

	private void sendMail(String email, String msg1, String msg2, String msg3, String msg4, String msg5, String msg6) {
		Session session = createSessionObject();

		try {//d
			//Message message = createMessage(email, subject, messageBody, session);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("weeework.studio@gmail.com", "spongebobsquarepants"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress("hunginda@gmail.com"));
			message.setSubject(judul);

			//3) create MimeBodyPart object and set your message content

			char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < 7; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}
			String output = sb.toString();
			System.out.println(output);

			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText("INVOICE : " + output);

			BodyPart messageBodyPart2 = new MimeBodyPart();
			messageBodyPart2.setText("Nama Lengkap = " + msg2);

			BodyPart messageBodyPart3 = new MimeBodyPart();
			messageBodyPart3.setText("Nomor HP = " + msg3);

			BodyPart messageBodyPart4 = new MimeBodyPart();
			messageBodyPart4.setText("Event Date = " + msg4);

			BodyPart messageBodyPart5 = new MimeBodyPart();
			messageBodyPart5.setText("Jumlah Orang = " + msg5);

			BodyPart messageBodyPart6 = new MimeBodyPart();
			messageBodyPart6.setText("Additional Info = " + msg6);

			//5) create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);
			multipart.addBodyPart(messageBodyPart3);
			multipart.addBodyPart(messageBodyPart4);
			multipart.addBodyPart(messageBodyPart5);
			multipart.addBodyPart(messageBodyPart6);
			//6) set the multiplart object to the message object
			message.setContent(multipart);



			new SendMailTask().execute(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static final String username = "weeework.studio@gmail.com";
	private static final String password = "spongebobsquarepants";


	private Session createSessionObject() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		return Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	private class SendMailTask extends AsyncTask<Message, Void, Void> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getContext(), "Mohon tunggu", "Sedang memproses....", true, false);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressDialog.dismiss();
			clearall();
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("Sukses!");
			builder.setMessage("Thank you for your order, we will call you soon for confirmation")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//do things
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

		}

		@Override
		protected Void doInBackground(Message... messages) {
			try {
				Transport.send(messages[0]);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void clearall() {

		msg.setText("");
		msg1.setText("");
		msg2.setText("");
		msg3.setText("");
		msg4.setText("");
	}

	private void enableSubmitIfReady() {

		boolean isReady = msg.getText().toString().length()>0 &&
				msg1.getText().toString().length()>0 &&
				msg2.getText().toString().length()>0 &&
				msg3.getText().toString().length()>0 &&
				msg4.getText().toString().length()>0;

		if (isReady){
			this.login.setBackgroundColor(getResources().getColor(R.color.white));
			this.login.setTextColor(getResources().getColor(R.color.color_bg_primary));
		} else{
			this.login.setBackgroundColor(getResources().getColor(R.color.grey));
		}
		login.setEnabled(isReady);

	}

	public static class displayDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int iYear = calendar.get(Calendar.YEAR);
			int iMonth = calendar.get(Calendar.MONTH);
			int iDay = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, iYear, iMonth, iDay);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			setSelectedDate(yy, mm + 1, dd);
		}
	}

	public static void setSelectedDate(int iYear, int iMonth, int iDay) {

		msg2.setText(iDay + "-" + iMonth + "-" + iYear);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mListFragments != null) {
			mListFragments.clear();
			mListFragments = null;
		}
	}
}
