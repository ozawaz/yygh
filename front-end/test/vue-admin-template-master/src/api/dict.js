import request from '@/utils/request' // 这个就是axios的实例对象

export default {
    // 根据id查询子数据
    dictList(id) {//数据字典列表
        return request ({
          url: `/admin/cmn/dict/findChildData/${id}`,
          method: 'get'
        })
    },

}