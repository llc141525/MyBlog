<template>
  <v-card class="mx-auto" elevation="10" max-width="600">
    <!-- 标题栏 -->
    <v-toolbar color="primary" dark>
      <v-toolbar-title>服务条款</v-toolbar-title>
    </v-toolbar>

    <!-- 服务条款内容 -->
    <v-card-text>
      <div class="text-h6 my-4">欢迎访问我的个人博客</div>
      <p>感谢您使用我的个人博客服务。以下条款和条件（"服务条款"）适用于您对我的个人博客的访问和使用。</p>
      <v-expansion-panels>
        <v-expansion-panel>
          <v-expansion-panel-title>1. 接受条款</v-expansion-panel-title>
          <v-expansion-panel-text>
            <p>通过访问或使用我的博客，您表示您已阅读、理解并同意受这些服务条款的约束。</p>
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel>
          <v-expansion-panel-title>2. 使用规则</v-expansion-panel-title>
          <v-expansion-panel-text>
            <p>您同意不使用我的博客进行任何非法或未经授权的目的。</p>
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel>
          <v-expansion-panel-title>3. 内容所有权</v-expansion-panel-title>
          <v-expansion-panel-text>
            <p>我的博客上的内容，包括但不限于文本、图片、视频等，均受版权保护。未经我的明确许可，您不得复制、修改或分发这些内容。</p>
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel>
          <v-expansion-panel-title>4. 免责声明</v-expansion-panel-title>
          <v-expansion-panel-text>
            <p>我的博客提供的信息仅供参考，不构成任何形式的建议或保证。对于因使用或无法使用我的博客而产生的任何损害，我概不负责。</p>
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel>
          <v-expansion-panel-title>5. 隐私政策</v-expansion-panel-title>
          <v-expansion-panel-text>
            <p>请查阅我的隐私政策，了解我如何收集、使用和保护您的个人信息。</p>
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel>
          <v-expansion-panel-title>6. 修改和终止</v-expansion-panel-title>
          <v-expansion-panel-text>
            <p>我保留随时修改或终止我的博客服务的权利。修改后的服务条款将在发布在我的博客上时生效。</p>
          </v-expansion-panel-text>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-card-text>

    <v-card-actions>
      <v-spacer />
      <v-btn color="primary" href="/auth/Register" @click="agreeTerms">同意</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
  export default {
    methods: {
      agreeTerms () {
        console.log('用户已同意服务条款');
      },
    },
  };
</script>

<style scoped>
.mx-auto {
  margin-top: 5rem;
  border-radius: 12px;
  overflow: hidden;
}
</style>
