<template>

  <v-card class="blog-post" flat>
    <!-- 图片区域 -->
    <div v-if="imageUrl" class="post-image mb-6">
      <img :alt="imageAlt" class="post-image-content" :src="imageUrl">
    </div>

    <!-- 标题区域 -->
    <div class="post-header mb-6">
      <div class="d-flex align-center">
        <h1 class="text-h3 font-weight-bold primary--text">{{ title }}</h1>
        <v-chip class="ml-3" color="secondary" small>{{ metaInfo }}</v-chip>
      </div>
      <h2 class="text-h6 grey--text text--darken-1 mt-2">{{ subtitle }}</h2>
    </div>

    <!-- 段落标题 -->
    <h3 class="text-h5 font-weight-medium mb-4">{{ paragraphTitle }}</h3>

    <!-- 正文内容 -->
    <div class="post-content">
      <p
        v-for="(paragraph, index) in contentParagraphs"
        :key="index"
        class="text-body-1 mb-5"
      >
        {{ paragraph }}
      </p>
    </div>
  </v-card>
</template>

<script>
  export default {
    props: {
      title: {
        type: String,
        default: 'Gadget',
      },
      subtitle: {
        type: String,
        default: 'Garmins Instinct Crossover is a rugged hybrid smartwatch',
      },
      metaInfo: {
        type: String,
        default: '149 3',
      },
      paragraphTitle: {
        type: String,
        default: 'Title of the paragraph',
      },
      content: {
        type: String,
        default: `But you cannot figure out what it is or what it can do. MTA web directory is the simplest way in which one can bid on a link, or a few links if they wish to do so. The link directory on MTA displays all of the links it currently has, and does so in alphabetical order, which makes it much easier for someone to find what they are looking for if it is something specific and they do not want to go through all the other sites and links as well. It allows you to start your bid at the bottom and slowly work your way to the top of the list.`,
      },
      imageUrl: {
        type: String,
        default: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg', // 默认无图片
      },
      imageAlt: {
        type: String,
        default: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
      },
    },
    computed: {
      contentParagraphs () {
        return this.content.split(/\n\s*\n/); // 按空行分割段落
      },
    },
  };
</script>

<style scoped>
.blog-post {
  max-width: 1200px;
  width: 95%;
  margin: 0 auto;
  padding: 32px;
  background-color: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

/* 图片区域样式 */
.post-image {
  width: calc(100% + 64px); /* 覆盖两侧内边距 */
  margin: -32px -32px 32px -32px; /* 负外边距覆盖卡片内边距 */
  border-radius: 12px 12px 0 0; /* 顶部圆角匹配卡片 */
  overflow: hidden; /* 确保图片不溢出圆角 */
  position: relative;
  background-color: #e0e0e0; /* 占位背景色 */
}

.post-image-content {
  width: 100%;
  height: auto;
  max-height: 500px; /* 最大高度限制 */
  display: block;
  object-fit: cover; /* 保持比例填充 */
  transition: transform 0.3s ease; /* 悬停动画 */
}

.post-image:hover .post-image-content {
  transform: scale(1.02); /* 悬停时轻微放大 */
}

/* 占位符样式 - 当没有图片时显示 */
.post-image:not([v-if])::before {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #757575;
  font-size: 1.2rem;
}

.post-header {
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 20px;
}

.post-content p {
  line-height: 1.9;
  text-align: justify;
  font-size: 1.1rem;
  color: #37474f;
}

.primary--text {
  color: #1565c0 !important;
}

.secondary {
  background-color: #7e57c2 !important;
  color: white !important;
  font-weight: 500;
}

/* 响应式调整 */
@media (max-width: 960px) {
  .blog-post {
    width: 100%;
    padding: 24px;
  }

  .post-image {
    width: calc(100% + 48px);
    margin: -24px -24px 24px -24px;
  }

  .text-h3 {
    font-size: 2rem !important;
  }
}

@media (max-width: 600px) {
  .post-image {
    border-radius: 8px 8px 0 0;
  }

  .post-image-content {
    max-height: 300px;
  }
}
</style>
