import request from '@/utils/request'

// 查询币种配置列表
export function listToken(query) {
  return request({
    url: '/system/token/list',
    method: 'get',
    params: query
  })
}

// 查询币种配置详细
export function getToken(id) {
  return request({
    url: '/system/token/' + id,
    method: 'get'
  })
}

// 新增币种配置
export function addToken(data) {
  return request({
    url: '/system/token',
    method: 'post',
    data: data
  })
}

// 修改币种配置
export function updateToken(data) {
  return request({
    url: '/system/token',
    method: 'put',
    data: data
  })
}

// 删除币种配置
export function delToken(id) {
  return request({
    url: '/system/token/' + id,
    method: 'delete'
  })
}
