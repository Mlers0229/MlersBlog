module.exports = {
  content: [
    "./app.vue",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  // 确保有 primary 颜色定义（你使用了 bg-primary）
  theme: {
    extend: {
      colors: {
        primary: '#3B82F6' // 示例蓝色值
      }
    }
  },
  plugins: [
    require('@tailwindcss/typography'),
  ]
}
   