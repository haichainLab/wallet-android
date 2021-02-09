package io.samos.wallet.activities.Transaction

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.common.OnItemClickListener
import io.samos.wallet.datas.Address
import io.samos.wallet.datas.Wallet
import io.samos.wallet.utils.SamosWalletUtils
import io.samos.wallet.utils.ToastUtils
import io.samos.wallet.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_rull_into.*
import mobile.Mobile

/**
 * @author: lh on 2018/5/21 17:28.
 * Email:luchefg@gmail.com
 * Description: 转入
 */
class RullIntoActivity : BaseActivity(), OnItemClickListener<Address> {

    lateinit var loadingDialog: LoadingDialog
    internal lateinit var wallet: Wallet
    var walletViewModel: WalletViewModel = WalletViewModel()

    lateinit var adapter: RullIntoAdapter

    override fun attachLayoutRes(): Int {
        return R.layout.activity_rull_into
    }


    override fun initViews() {

        loadingDialog = LoadingDialog(this)
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        btn_new_adress.setOnClickListener {
            newAddress()
        }
        refresh.setOnRefreshListener {
            refreshDatas()
        }

        rec_view.layoutManager = LinearLayoutManager(this)
        adapter = RullIntoAdapter(null)
        rec_view.adapter = adapter
        adapter.onItemClick = this

    }

    fun newAddress() {
        loadingDialog.show(resources.getString(R.string.creating_new_address))
        Observable.create(ObservableOnSubscribe<String> {
            it.onNext(Mobile.newAddress(wallet.walletID, 1, SamosWalletUtils.getPin16()))
            it.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        loadingDialog.dismiss()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                        refresh.post {
                            refresh.isRefreshing = true
                            refreshDatas()
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadingDialog.dismiss()
//                        ToastUtils.show(e.message)
                    }

                })
    }

    override fun initData() {
        wallet = intent.getSerializableExtra(Constant.KEY_WALLET) as Wallet

        //lwj 0.0.1 HAI->HAIC
        if(wallet.token.equals("HAI")){
            id_toolbar_title.text = resources.getString(R.string.into) + " HAIC"

        }else{
            id_toolbar_title.text = resources.getString(R.string.into) + " " + wallet.token

        }


//        if (wallet.walletType.equals(Constant.COIN_TYPE_SAMO)) {
//            id_toolbar_title.text = resources.getString(R.string.into) + " " + Constant.COIN_SAMO
//        } else if (wallet.walletType.equals(Constant.COIN_TYPE_SKY)) {
//            id_toolbar_title.text = resources.getString(R.string.into) + " " + Constant.COIN_SKY
//        }else if (wallet.walletType.equals(Constant.COIN_TYPE_YBC)) {
//            id_toolbar_title.text = resources.getString(R.string.into) + " " + Constant.COIN_YBC
//        }else if (wallet.walletType.equals(Constant.COIN_TYPE_SHC)) {
//            id_toolbar_title.text = resources.getString(R.string.into) + " " + Constant.COIN_SHC
//        }
        refresh.post {
            refresh.isRefreshing = true
            refreshDatas()
        }
    }

    override fun onClick(viewId: Int, positon: Int, bean: Address?) {
        intent = Intent(this, IntoWalletActivity::class.java)
        intent.putExtra(Constant.KEY_ADDRESS, bean!!.address)
        intent.putExtra(Constant.TOKEN_TYPE, wallet.token)
        startActivity(intent)
    }

    fun refreshDatas() {
        walletViewModel.getAllAddressByWalletId(wallet.tokenName, wallet.walletID)
                .compose(this.bindUntilEvent<List<Address>>(ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<Address>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(addresses: List<Address>) {
                        adapter.note = addresses
                        adapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        onComplete()
                    }

                    override fun onComplete() {
                        refresh.isRefreshing = false
                    }
                })
    }
}