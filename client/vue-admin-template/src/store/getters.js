const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  username: state => state.user.username,
  code: state => state.user.code,
  roleName: state => state.user.roleName,
  routes: state => state.user.routes,
}
export default getters
