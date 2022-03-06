const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  code: state => state.user.code,
  role: state => state.user.role,
  routes: state => state.user.routes,
}
export default getters
