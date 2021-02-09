package io.samos.wallet.activities.Assets

/**
 * @author: lh on 2018/5/21 15:26.
 * Email:luchefg@gmail.com
 * Description:  资产
 */
interface AddAssetsListener {


    /**
     * 选中事件
     * @param position
     * @param bean
     */
    fun onCheckClick(position: Int,boo: Boolean, bean: AssetsTypeBean)



}
