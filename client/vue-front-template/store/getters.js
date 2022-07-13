const getters = {
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  nickname: state => state.user.nickname,
  uid: state => state.user.uid,
  code: state => state.user.code,
  role: state => state.user.role,
}
export default getters
