package io.samos.wallet.activities.Assets

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.*
import io.samos.wallet.utils.LogUtils
import io.samos.wallet.utils.SharePrefrencesUtil
import io.samos.wallet.utils.ToastUtils
import io.samos.wallet.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_add_newassets.*
import kotlinx.android.synthetic.main.item_setting.*
import mobile.Mobile
import java.util.*

/**
 * @author: lh on 2018/5/17 15:11.
 * Email:luchefg@gmail.com
 * Description: 添加新资产
 */
class AddNewAssetsActivity : BaseActivity(), AddAssetsListener {

    internal lateinit var addAdapter: AddAssetsAdapter
    val list = ArrayList<AssetsTypeBean>()
    var walletName: String = WalletManager.getInstance().restorDefaultWalletDB().name
    lateinit var WalletViewModel: WalletViewModel
    lateinit var bean: AssetsTypeBean
    lateinit var dialog: LoadingDialog
    lateinit var wallet: Wallet
//    lateinit var walletIndex: WalletIndex
    lateinit var walletDb: WalletDB


    override fun attachLayoutRes(): Int {
        return R.layout.activity_add_newassets
    }

    override fun initViews() {
        id_toolbar_title.text = resources.getString(R.string.add_assets)
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        walletDb = WalletManager.getInstance().restorDefaultWalletDB()
        dialog = LoadingDialog(this)
        WalletViewModel = WalletViewModel()
        rec_assets.layoutManager = LinearLayoutManager(this)
        addAdapter = AddAssetsAdapter(list)
        addAdapter.assetListener = this
        rec_assets.adapter = addAdapter
        setData()

    }

    /**
     * 加载网络配置里面新支持的币种
     */
    fun setData() {
        val walletCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
        val walletCfg = Gson().fromJson<TokenSet>(walletCfgJson,
                object : TypeToken<TokenSet>() {}.type)


        val mutableListOf = mutableListOf<AssetsTypeBean>()

        walletCfg.tokens.forEachIndexed { index, tokenCfg ->

            var wlt = walletDb.getWlt(tokenCfg.token)
            var selected = false
            if(wlt != null ) {
                selected =  wlt.isSelected
            }

            //lwj 0.0.1 HAI->HAIC
            if(tokenCfg.token.equals("HAI")){
                mutableListOf.add(AssetsTypeBean(tokenCfg.tokenName, "HAIC",
                        selected, index.toString(),tokenCfg.tokenIcon))
            }else{
                mutableListOf.add(AssetsTypeBean(tokenCfg.tokenName, tokenCfg.token,
                        selected, index.toString(),tokenCfg.tokenIcon))
            }


        }


        addAdapter.assets = mutableListOf
        addAdapter.notifyDataSetChanged()
    }

    override fun onCheckClick(position: Int, boo: Boolean, bean: AssetsTypeBean) {


        var wlt = walletDb.getWalltByTokenName(bean.tokenName)
        if(wlt == null) {
            newWallet(bean.tokenName!!)
        } else {


            LogUtils.d("checkt asset:"+wlt.token)
            if (boo) { //选择了
                if(!Mobile.isExist( wlt.walletID)) {//avoid exception edge case
                    newWallet(bean.tokenName!!)
                }
                wlt.selected = true;

            } else {
                wlt.selected = false
            }
            walletDb.addWallet(wlt.token, wlt)
            WalletManager.getInstance().saveDefaultWalletDB(walletDb)
            WalletManager.getInstance().updateWalletDB(walletDb.name, walletDb)//更新index-》通知界面做变化

        }


    }


    fun newWallet(tokenName: String) {
            dialog.show(resources.getString(R.string.loading_str))
            WalletViewModel.getWalletSeed(wallet.walletID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                    }.subscribeOn(Schedulers.io())
                    .flatMap { s -> WalletViewModel.addWallet(this, tokenName, wallet.walletName, s) }.subscribe({
                       // walletDb = WalletManager.getInstance().restorDefaultWalletDB() //重置
                        dialog.dismiss()
                        finish()
                    }) {
                      //  it.printStackTrace()
                        //ToastUtils.show(it.message)
                        dialog.dismiss()
                    }


    }

    override fun initData() {
        wallet = intent.getSerializableExtra(Constant.KEY_WALLET) as Wallet
    }
}