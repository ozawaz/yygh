import request from '@/utils/request' // 这个就是axios的实例对象

export default {
    // 分页查询
    getHospSetList (current,limit,seacrchObj) {
        return request({
            url: `/admin/hosp/hospitalSet/findPageHospSet/${current}/${limit}`,
            method: 'post',
            data: seacrchObj // json数据对象
        })
    },

    // 根据id删除
    deleteHospSet(id) {
        return request ({
          url: `/admin/hosp/hospitalSet/${id}`,
          method: 'delete'
        })
    },
    
    // 批量删除
    batchRemoveHospSet(idList) {
        return request({
            url: `/admin/hosp/hospitalSet/batchRemove`,
            method: 'delete',
            data: idList
        })
    },

    // 状态锁定
    lockHospSet(id, status) {
        return request({
            url: `/admin/hosp/hospitalSet/lockHospitalSet/${id}/${status}`,
            method: 'put'
        })
    },

    //添加医院设置
    saveHospSet(hospitalSet) {
        return request ({
        url: `/admin/hosp/hospitalSet/saveHospitalSet`,
        method: 'post',
        data: hospitalSet
        })
    },

    //修改医院设置
    updateHospSet(hospitalSet) {
        return request ({
        url: `/admin/hosp/hospitalSet/updateHospitalSet`,
        method: 'post',
        data: hospitalSet
        })
    },

    //医院设置id查询
    getHospSet(id) {
        return request ({
        url: `/admin/hosp/hospitalSet/getHospSet/${id}`,
        method: 'get'
        })
    },


      
}