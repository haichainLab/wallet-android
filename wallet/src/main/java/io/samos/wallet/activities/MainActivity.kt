package io.samos.wallet.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zxing.lib.CaptureActivity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.mall.H5Deposit
import io.samos.wallet.R
import io.samos.wallet.activities.Main.MeFragment
import io.samos.wallet.activities.Main.WalletFragment
import io.samos.wallet.activities.Main.WalletSlideAdapter
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.activities.ManagerWallet.ManagerWalletActivity
import io.samos.wallet.activities.Wallet.WalletInitActivity
import io.samos.wallet.activities.found.FoundFragment
import io.samos.wallet.activities.WebViews.VoteFragment
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.common.OnItemClickListener
import io.samos.wallet.datas.WalletDB
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_add_newassets.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_wallet.*
import kotlinx.android.synthetic.main.layout_navigation.*
import java.util.*

/**
 * 主界面，一级界面
 * edit by 方丈 2018/05/10
 */
class MainActivity : BaseActivity(), OnItemClickListener<WalletDB> {
    /** 主页面fragment */
    private lateinit var mainWalletFragment: WalletFragment
    /** 我的fragment */
    private lateinit var meFragment: MeFragment
    /** 发现fragment */
    private lateinit var foundFragment: FoundFragment

    //lwj 2.1.1
    /** 投票fragment*/
    private lateinit var voteFragment: VoteFragment

    /** 主页面Adapter */
    internal lateinit var mainWalletAdapter: WalletSlideAdapter

    /** 主页面data */
    private var mainWalletBeans: List<WalletDB> = ArrayList()

    /** ViewModel */
    private var walletViewModel = WalletViewModel()

    override fun isStatusBarBlack(): Boolean = false
    override fun isTranslucent(): Boolean = true

    /**
     *  然后执行
     */
    override fun initData() {
        toolbar_title?.text = WalletManager.getInstance().restorDefaultWalletDB()!!.name
    }

    /**
     * 返回布局文件
     */
    override fun attachLayoutRes(): Int {
        return R.layout.activity_main
    }

    /**
     *初始化界面 ，首先执行
     */
    override fun initViews() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, main_toolbar, R.string.Address, R.string.Address)
        drawer_layout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        main_toolbar?.setNavigationIcon(R.drawable.menu)


        val bundle = Bundle()
        bundle.putSerializable("WALLET_DB", WalletManager.getInstance().restorDefaultWalletDB())
        //默认钱包的配置数据

        mainWalletFragment = WalletFragment.newInstance(null)
        meFragment = MeFragment.newInstance(bundle)

        foundFragment = FoundFragment.newInstance(null)
//        voteFragment = VoteFragment.newInstance(null)

        //不需要发现

        //设置底部导航按钮的点击事件--钱包fragment
        lly_wallet?.setOnClickListener {

            //lwj 2.1.1
            val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, main_toolbar, R.string.Address, R.string.Address)
            drawer_layout?.addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.syncState()
            main_toolbar?.setNavigationIcon(R.drawable.menu)

            switchFragment(mainWalletFragment)
            iv_wallet.isSelected = true
            lly_wallet.isSelected = true
            iv_me.isSelected = false
            lly_me.isSelected = false
//            iv_found.isSelected = false
//            lly_found.isSelected = false
            toolbar_title_me.visibility = View.GONE
            toolbar_iv_refresh.visibility = View.GONE
            toolbar_title.visibility = View.VISIBLE
            main_toolbar?.setNavigationIcon(R.drawable.menu)
            //允许drawerLayout滑出
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
      //  设置底部导航按钮的点击事件--发现fragment
//        lly_found?.setOnClickListener {
//
//            switchFragment(foundFragment)
//            iv_wallet.isSelected = false
//            lly_wallet.isSelected = false
//            iv_me.isSelected = false
//            lly_me.isSelected = false
//            iv_found.isSelected = true
//            toolbar_title_me.visibility = View.VISIBLE
//            toolbar_title_me.setText(R.string.found)
//            toolbar_iv_refresh.visibility = View.VISIBLE
//            main_toolbar?.navigationIcon = ColorDrawable(Color.TRANSPARENT)
//            //禁止drawerLayout滑出
//            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//        }
        toolbar_iv_refresh?.setOnClickListener {
            //lwj 2.1.1
            voteFragment.getWebView()?.reload()
        }

        //设置底部导航按钮的点击事件--我的fragment
        lly_me?.setOnClickListener {
            switchFragment(meFragment)
            iv_wallet.isSelected = false
            lly_wallet.isSelected = false
            iv_me.isSelected = true
            lly_me.isSelected = true
//            iv_found.isSelected = false
//            lly_found.isSelected = false
            toolbar_title_me.visibility = View.VISIBLE
            toolbar_title_me.setText(R.string.me)
            toolbar_title.visibility = View.GONE

            toolbar_iv_refresh.visibility = View.GONE

            main_toolbar?.navigationIcon = ColorDrawable(Color.TRANSPARENT)
            //禁止drawerLayout滑出
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        tv_managerwallet.setOnClickListener {
            val intent = Intent(this, ManagerWalletActivity::class.java)
            startActivity(intent)
        }
        switchFragment(mainWalletFragment)
        iv_wallet.isSelected = true
        lly_wallet.isSelected = true
        lin_qr.setOnClickListener {
            val intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, Constant.REQUEST_QRCODE)
            drawer_layout.closeDrawer(lin_content)
        }
        lin_create.setOnClickListener {
            intent = Intent(this, WalletInitActivity::class.java)
            intent.putExtra("STARTWAY", "CREATE")
            startActivity(intent)
        }
        mainWalletAdapter = WalletSlideAdapter(mainWalletBeans)
        wallet_list.adapter = mainWalletAdapter
        mainWalletAdapter.mainWalletListener = this
        wallet_list.layoutManager = LinearLayoutManager(this)

        //刷新
        refreshWalletDatas()

    }

    override fun onClick(viewId: Int, positon: Int, bean: WalletDB?) {
        for (i in mainWalletAdapter.walletDBList!!.indices) {
            mainWalletAdapter.walletDBList!![i].isSelected = positon == i
        }
        mainWalletAdapter.notifyDataSetChanged()
        WalletManager.getInstance().saveDefaultWalletDB(bean)
        toolbar_title?.text = bean!!.name
        drawer_layout.closeDrawer(lin_content)
        mainWalletFragment.updateWalletDB(WalletManager.getInstance().restorDefaultWalletDB())
    }

    /**
     * 切换碎片视图
     * @param fragment 需要切换的fragment
     */
    private fun switchFragment(fragment: Fragment) {
        val tag = fragment.javaClass.simpleName
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val instance = supportFragmentManager.findFragmentByTag(tag)
        if (instance == null) {
            fragmentTransaction.add(R.id.main_container, fragment, tag)
        }
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (item in fragments) {
                if (item == instance) {
                    fragmentTransaction.show(item)
                } else {
                    fragmentTransaction.hide(item)
                }
            }
        }

        //lwj 2.1.1 切换到其他fragment时先删除webviewfragment
        val instanceVoteFragment = supportFragmentManager.findFragmentByTag("VoteFragment")
        if (instanceVoteFragment != null) {
            fragmentTransaction.remove(instanceVoteFragment)
        }

        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        refreshWalletDatas()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_QRCODE && resultCode == Activity.RESULT_OK) {

            val code = data!!.data!!.toString()

            //lwj 2.1.1 扫码投票页面
            // url 格式为https://appapi.haicshop.com/front/vote.php?vote_no=2020Q101
            if(code!=null && code.split("?")[1].split("=")[0].equals("vote_no"))
            {
                var voteUrl = code.split("?")[0]
                var voteNo = code.split("?")[1].split("=")[1]


                voteFragment = VoteFragment.newInstance(null)
                voteFragment.setVoteParam(voteUrl,voteNo)
                switchFragment(voteFragment)


                iv_wallet.isSelected = false
                lly_wallet.isSelected = false
                iv_me.isSelected = false
                lly_me.isSelected = false
                toolbar_title.visibility = View.GONE
                toolbar_title_me.visibility = View.VISIBLE
                toolbar_title_me.setText(R.string.vote)
                toolbar_iv_refresh.visibility = View.VISIBLE

                main_toolbar?.setNavigationIcon(R.drawable.web_back)
                main_toolbar.setNavigationOnClickListener({
                    voteFragment.getWebView()?.goBack()
                })


            }else if(code!=null && code.split("=").size>3) {
                mainWalletFragment.satrtRullOut(code)
            } else {
                ToastUtils.show("二维码缺少币种类型等参数，请从主界面特定的币种进入扫描")

            }
        }
    }

    //切换DB
    private fun refreshWalletDatas() {
        walletViewModel.allWalletDB
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<WalletDB>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(walletDBList: List<WalletDB>) {
                        var walletDB = WalletManager.getInstance().restorDefaultWalletDB()
                        for (i in walletDBList.indices) {
                            if (walletDB == null) {
                                walletDBList[0].isSelected = true
                                WalletManager.getInstance().saveDefaultWalletDB(walletDBList[0])
                            } else {
                                walletDBList[i].isSelected = walletDBList[i].name.equals(walletDB.name)
                            }
                        }
                        mainWalletAdapter.walletDBList = walletDBList
                        mainWalletAdapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        swiprefreshlayout.isRefreshing = false
                        if (WalletManager.getInstance().restorDefaultWalletDB() == null) {
                            return
                        }
                        toolbar_title.text = WalletManager.getInstance().restorDefaultWalletDB().name
                        mainWalletFragment.updateWalletDB(WalletManager.getInstance().restorDefaultWalletDB())
                    }
                })
    }
}
