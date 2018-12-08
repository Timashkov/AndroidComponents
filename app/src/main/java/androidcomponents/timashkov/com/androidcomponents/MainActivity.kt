package androidcomponents.timashkov.com.androidcomponents

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moneyEdit.addTextChangedListener(MoneyTextWatcher(moneyEdit))

        phoneEdit.addTextChangedListener(PhoneTextWatcher(phoneEdit))

        emailEdit.addTextChangedListener(EmailTextWatcher(emailEdit))
    }
}

//
//
//package ru.emotion24.velorent.setup.fragments
//
//import android.Manifest
//import android.content.Context
//import android.content.DialogInterface
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AlertDialog
//import android.telephony.TelephonyManager
//import android.text.Editable
//import android.text.InputFilter
//import android.text.TextUtils
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import ru.emotion24.velorent.R
//import ru.emotion24.velorent.core.data.ErrorInfo
//import ru.emotion24.velorent.core.dataservices.IUserService
//import ru.emotion24.velorent.core.util.Preferences
//import ru.emotion24.velorent.setup.SetupActivity
//import ru.emotion24.velorent.setup.presenters.SetupActivityRouterContract
//import ru.emotion24.velorent.setup.presenters.SignInPresenter
//import ru.emotion24.velorent.setup.presenters.SignInPresenterContract
//import ru.emotion24.velorent.ui.base.BaseActivity
//import ru.emotion24.velorent.ui.base.BaseFragment
//import javax.inject.Inject
//import kotlinx.android.synthetic.main.fragment_sign_in.*
//import ru.emotion24.velorent.setup.dialogs.PasswordRecoverCode
//
///**
// * Created by Aleksei Timashkov on 07.02.2018.
// */
//class SignInFragment : BaseFragment(), SignInPresenterContract.View {
//
//    private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 1
//
//    @field:[Inject]
//    lateinit var mRouter: SetupActivityRouterContract.Presenter
//
//    @field:[Inject]
//    lateinit var mUserService: IUserService
//
//    private var mTempFilteringString = ""
//
//    lateinit var mSignInPresenter: SignInPresenterContract.Presenter
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        activity?.let {
//            (it as BaseActivity).injectFragment(this, SignInFragment.FRAGMENT_TAG)
//            mSignInPresenter = SignInPresenter(mUserService, mRouter, it)
//            mSignInPresenter.attachView(this)
//            mSignInPresenter.onRestoreInstance(savedInstanceState)
//
//            (it as SetupActivity).setTitle(R.string.register)
//            et_signin_phone.editText?.addTextChangedListener(object : EditTextValueWatcher() {
//                override fun afterTextChanged(s: Editable?) {
//                    super.afterTextChanged(s)
//                    s?.let {
//                        val userInput = s.toString().replace("[^\\d|+]".toRegex(), "")
//                        var c = 0
//                        if (userInput.isNotEmpty() && userInput[0] == '+')
//                            c = 1
//                        if (s.toString() != mTempFilteringString) {
//                            if (userInput.length <= 11 + c) {
//                                val sb = StringBuilder()
//                                for (i in 0 until userInput.length) {
//                                    if (i != 0 && userInput[i] == '+')
//                                        continue
//
//                                    if (i in arrayOf(1 + c, 4 + c, 7 + c, 9 + c)) {
//                                        sb.append(" ")
//                                    }
//                                    sb.append(userInput[i])
//                                }
//                                mTempFilteringString = sb.toString()
//
//                                s.filters = arrayOfNulls<InputFilter>(0)
//                            }
//                            s.replace(0, s.length, mTempFilteringString, 0, mTempFilteringString.length)
//                        }
//                        btn_verify_phone.isEnabled = userInput.length == 11 + c
//                        et_signin_phone.isErrorEnabled = false
//                        mSignInPresenter.phoneValue(userInput)
//                    }
//                }
//            })
//
//            et_signin_password.editText?.addTextChangedListener(object : EditTextValueWatcher() {
//                override fun afterTextChanged(s: Editable?) {
//                    super.afterTextChanged(s)
//                    et_signin_password.isErrorEnabled = false
//                    mSignInPresenter.passwordValue(s.toString())
//                }
//            })
//
//            if (Preferences.getPhoneNumber(it).isEmpty()) {
//                val permissionCheck = ContextCompat.checkSelfPermission(it, Manifest.permission.READ_PHONE_STATE)
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(
//                        arrayOf(Manifest.permission.READ_PHONE_STATE),
//                        PERMISSIONS_REQUEST_READ_PHONE_STATE)
//                } else {
//                    showPhoneNumber()
//                }
//            } else {
//                et_signin_phone.editText?.setText(Preferences.getPhoneNumber(it))
//            }
//
//            btn_login.setOnClickListener { mSignInPresenter.login() }
//            btn_verify_phone.setOnClickListener { mSignInPresenter.checkPhoneExists() }
//            tv_password_forgotten.setOnClickListener { mSignInPresenter.passwordLost() }
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mSignInPresenter.onSaveInstanceState(outState)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
//        butter(view)
//
//        return view
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mSignInPresenter.detachView()
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showPhoneNumber()
//            }
//        }
//    }
//
//    override fun toggleProgress(enabled: Boolean) {
//        (activity as SetupActivity).toggleProgressBar(enabled)
//        btn_verify_phone.isEnabled = !enabled
//        et_signin_phone.isEnabled = !enabled
//
//        //HIDE ERROR
//    }
//
//    override fun showError(errorInfo: ErrorInfo) {
//        (activity as SetupActivity).toggleError(errorInfo)
//    }
//
//
//    override fun showPhoneExists() {
//        activity?.let {
//            val askDialog = AlertDialog.Builder(it, R.style.VeloDialogTheme)
//                .setMessage(getString(R.string.dialog_proceed_with_this_phone, et_signin_phone.editText?.text.toString()))
//                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener() { _, _ -> showLoginForm() })
//                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener() { _, _ -> et_signin_phone.editText?.clearComposingText() })
//                .create()
//            askDialog.show()
//        }
//    }
//
//    override fun getBaseFragmentTag(): String {
//        return FRAGMENT_TAG
//    }
//
//    private fun showLoginForm() {
//        with(et_signin_password) {
//            visibility = View.VISIBLE
//            isEnabled = true
//        }
//        btn_verify_phone.visibility = View.GONE
//        btn_login.visibility = View.VISIBLE
//        tv_password_forgotten.visibility = View.VISIBLE
//        et_signin_phone.isEnabled = false
//    }
//
//    override fun hidePasswordField() {
//        with(et_signin_password) {
//            editText?.clearComposingText()
//            visibility = View.GONE
//        }
//    }
//
//    override fun showLocalError(msg: String) {
//        with(et_signin_password) {
//            isErrorEnabled = true
//            error = if (TextUtils.equals(msg.toLowerCase(), "bad credentials"))
//                getString(R.string.wrong_password)
//            else
//                msg
//        }
//    }
//
//    override fun showRequestForEnterSmsCodePF() {
//        val phoneNum = et_signin_phone.editText?.text.toString()
//        val dialog = PasswordRecoverCode.newInstance(phoneNum)
//        dialog.setOnClickListener(object : PasswordRecoverCode.OnDoneClick {
//            override fun onClick(otp: String) {
//                mSignInPresenter.proceedPasswordRecoverWithCode(otp)
//            }
//        })
//        dialog.show(fragmentManager, PasswordRecoverCode.DIALOG_TAG)
//    }
//
//    override fun notifyUserPasswordRecovered() {
//        with(et_signin_password) {
//            isErrorEnabled = false
//            editText?.clearComposingText()
//        }
//        activity?.let {
//            val notificationDialog = AlertDialog.Builder(it, R.style.VeloDialogTheme)
//                .setMessage(getString(R.string.password_recover_wait_new_password))
//                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener() { _, _ -> })
//                .create()
//            notificationDialog.show()
//        }
//    }
//
//    private fun showPhoneNumber() {
//        try {
//            val tMgr = activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            val text = et_signin_phone.editText?.text
//
//            if (TextUtils.isEmpty(text))
//                et_signin_phone.editText?.setText(tMgr.line1Number)
//        } catch (e: SecurityException) {
//        }
//    }
//
//
//    companion object {
//        internal val FRAGMENT_TAG = SignInFragment::class.java.name
//        fun getInstance(): SignInFragment = SignInFragment()
//    }
//}