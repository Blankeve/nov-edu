import defaultSettings from '@/settings'

const title = defaultSettings.title || 'NOV在线课堂后台管理系统'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
