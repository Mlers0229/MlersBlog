import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useAuthStore } from './auth';

export const useInteractionStore = defineStore('interaction', () => {
  const authStore = useAuthStore();
  
  // 状态
  const postLikes = ref({}); // 文章点赞：{ postId: { count: 10, users: [1, 2, 3] } }
  const postFavorites = ref({}); // 文章收藏：{ postId: { count: 5, users: [1, 2] } }
  const commentLikes = ref({}); // 评论点赞：{ commentId: { count: 3, users: [1, 2] } }
  
  // 用户互动列表
  const userLikedPosts = ref([]); // 用户点赞的文章ID列表
  const userFavoritedPosts = ref([]); // 用户收藏的文章ID列表
  const userLikedComments = ref([]); // 用户点赞的评论ID列表
  
  // 初始化本地存储数据
  const initialize = () => {
    if (process.client) {
      try {
        // 加载互动数据
        const storedPostLikes = localStorage.getItem('blog_post_likes');
        const storedPostFavorites = localStorage.getItem('blog_post_favorites');
        const storedCommentLikes = localStorage.getItem('blog_comment_likes');
        
        if (storedPostLikes) postLikes.value = JSON.parse(storedPostLikes);
        if (storedPostFavorites) postFavorites.value = JSON.parse(storedPostFavorites);
        if (storedCommentLikes) commentLikes.value = JSON.parse(storedCommentLikes);
        
        // 加载用户互动列表
        if (authStore.isLoggedIn && authStore.user) {
          const userId = authStore.user.id;
          const storedUserLikedPosts = localStorage.getItem(`blog_user_${userId}_liked_posts`);
          const storedUserFavoritedPosts = localStorage.getItem(`blog_user_${userId}_favorited_posts`);
          const storedUserLikedComments = localStorage.getItem(`blog_user_${userId}_liked_comments`);
          
          if (storedUserLikedPosts) userLikedPosts.value = JSON.parse(storedUserLikedPosts);
          if (storedUserFavoritedPosts) userFavoritedPosts.value = JSON.parse(storedUserFavoritedPosts);
          if (storedUserLikedComments) userLikedComments.value = JSON.parse(storedUserLikedComments);
        }
      } catch (error) {
        console.error('加载互动数据失败:', error);
      }
    }
  };
  
  // 保存互动数据到本地存储
  const saveInteractionData = () => {
    if (process.client) {
      try {
        // 保存互动数据
        localStorage.setItem('blog_post_likes', JSON.stringify(postLikes.value));
        localStorage.setItem('blog_post_favorites', JSON.stringify(postFavorites.value));
        localStorage.setItem('blog_comment_likes', JSON.stringify(commentLikes.value));
        
        // 保存用户互动列表
        if (authStore.isLoggedIn && authStore.user) {
          const userId = authStore.user.id;
          localStorage.setItem(`blog_user_${userId}_liked_posts`, JSON.stringify(userLikedPosts.value));
          localStorage.setItem(`blog_user_${userId}_favorited_posts`, JSON.stringify(userFavoritedPosts.value));
          localStorage.setItem(`blog_user_${userId}_liked_comments`, JSON.stringify(userLikedComments.value));
        }
      } catch (error) {
        console.error('保存互动数据失败:', error);
      }
    }
  };
  
  // 点赞文章
  const togglePostLike = (postId, postTitle, authorId) => {
    if (!authStore.isLoggedIn || !authStore.user) return false;
    
    const userId = authStore.user.id;
    const isLiked = userLikedPosts.value.includes(postId);
    
    // 更新用户点赞列表
    if (isLiked) {
      userLikedPosts.value = userLikedPosts.value.filter(id => id !== postId);
    } else {
      userLikedPosts.value.push(postId);
    }
    
    // 更新文章点赞数据
    if (!postLikes.value[postId]) {
      postLikes.value[postId] = { count: 0, users: [] };
    }
    
    if (isLiked) {
      // 取消点赞
      postLikes.value[postId].count = Math.max(0, postLikes.value[postId].count - 1);
      postLikes.value[postId].users = postLikes.value[postId].users.filter(id => id !== userId);
    } else {
      // 添加点赞
      postLikes.value[postId].count++;
      if (!postLikes.value[postId].users.includes(userId)) {
        postLikes.value[postId].users.push(userId);
      }
      
      // 发送通知（仅在添加点赞时）
      if (authorId && authorId !== userId) {
        // 通过事件总线发送通知
        if (typeof window !== 'undefined') {
          window.dispatchEvent(new CustomEvent('add-notification', {
            detail: {
              type: 'like',
              actor: authStore.user,
              target: {
                id: postId,
                title: postTitle || `文章 #${postId}`
              }
            }
          }));
        }
      }
    }
    
    // 保存数据
    saveInteractionData();
    return !isLiked; // 返回当前状态
  };
  
  // 收藏文章
  const togglePostFavorite = (postId, postTitle, authorId) => {
    if (!authStore.isLoggedIn || !authStore.user) return false;
    
    const userId = authStore.user.id;
    const isFavorited = userFavoritedPosts.value.includes(postId);
    
    // 更新用户收藏列表
    if (isFavorited) {
      userFavoritedPosts.value = userFavoritedPosts.value.filter(id => id !== postId);
    } else {
      userFavoritedPosts.value.push(postId);
    }
    
    // 更新文章收藏数据
    if (!postFavorites.value[postId]) {
      postFavorites.value[postId] = { count: 0, users: [] };
    }
    
    if (isFavorited) {
      // 取消收藏
      postFavorites.value[postId].count = Math.max(0, postFavorites.value[postId].count - 1);
      postFavorites.value[postId].users = postFavorites.value[postId].users.filter(id => id !== userId);
    } else {
      // 添加收藏
      postFavorites.value[postId].count++;
      if (!postFavorites.value[postId].users.includes(userId)) {
        postFavorites.value[postId].users.push(userId);
      }
      
      // 发送通知（仅在添加收藏时）
      if (authorId && authorId !== userId) {
        // 通过事件总线发送通知
        if (typeof window !== 'undefined') {
          window.dispatchEvent(new CustomEvent('add-notification', {
            detail: {
              type: 'favorite',
              actor: authStore.user,
              target: {
                id: postId,
                title: postTitle || `文章 #${postId}`
              }
            }
          }));
        }
      }
    }
    
    // 保存数据
    saveInteractionData();
    return !isFavorited; // 返回当前状态
  };
  
  // 点赞评论
  const toggleCommentLike = (commentId, postId, postTitle, authorId) => {
    if (!authStore.isLoggedIn || !authStore.user) return false;
    
    const userId = authStore.user.id;
    const isLiked = userLikedComments.value.includes(commentId);
    
    // 更新用户评论点赞列表
    if (isLiked) {
      userLikedComments.value = userLikedComments.value.filter(id => id !== commentId);
    } else {
      userLikedComments.value.push(commentId);
    }
    
    // 更新评论点赞数据
    if (!commentLikes.value[commentId]) {
      commentLikes.value[commentId] = { count: 0, users: [] };
    }
    
    if (isLiked) {
      // 取消点赞
      commentLikes.value[commentId].count = Math.max(0, commentLikes.value[commentId].count - 1);
      commentLikes.value[commentId].users = commentLikes.value[commentId].users.filter(id => id !== userId);
    } else {
      // 添加点赞
      commentLikes.value[commentId].count++;
      if (!commentLikes.value[commentId].users.includes(userId)) {
        commentLikes.value[commentId].users.push(userId);
      }
      
      // 发送通知（仅在添加点赞时）
      if (authorId && authorId !== userId) {
        // 通过事件总线发送通知
        if (typeof window !== 'undefined') {
          window.dispatchEvent(new CustomEvent('add-notification', {
            detail: {
              type: 'commentLike',
              actor: authStore.user,
              target: {
                id: postId,
                title: postTitle || `文章 #${postId}`
              },
              commentId: commentId
            }
          }));
        }
      }
    }
    
    // 保存数据
    saveInteractionData();
    return !isLiked; // 返回当前状态
  };
  
  // 检查用户是否点赞文章
  const isPostLiked = (postId) => {
    return userLikedPosts.value.includes(postId);
  };
  
  // 检查用户是否收藏文章
  const isPostFavorited = (postId) => {
    return userFavoritedPosts.value.includes(postId);
  };
  
  // 检查用户是否点赞评论
  const isCommentLiked = (commentId) => {
    return userLikedComments.value.includes(commentId);
  };
  
  // 获取文章点赞数量
  const getPostLikeCount = (postId) => {
    return postLikes.value[postId]?.count || 0;
  };
  
  // 获取文章收藏数量
  const getPostFavoriteCount = (postId) => {
    return postFavorites.value[postId]?.count || 0;
  };
  
  // 获取评论点赞数量
  const getCommentLikeCount = (commentId) => {
    return commentLikes.value[commentId]?.count || 0;
  };
  
  // 获取点赞文章的用户ID列表
  const getPostLikeUserIds = (postId) => {
    return postLikes.value[postId]?.users || [];
  };
  
  // 获取收藏文章的用户ID列表
  const getPostFavoriteUserIds = (postId) => {
    return postFavorites.value[postId]?.users || [];
  };
  
  // 获取点赞评论的用户ID列表
  const getCommentLikeUserIds = (commentId) => {
    return commentLikes.value[commentId]?.users || [];
  };
  
  return {
    initialize,
    togglePostLike,
    togglePostFavorite,
    toggleCommentLike,
    isPostLiked,
    isPostFavorited,
    isCommentLiked,
    getPostLikeCount,
    getPostFavoriteCount,
    getCommentLikeCount,
    getPostLikeUserIds,
    getPostFavoriteUserIds,
    getCommentLikeUserIds,
    userLikedPosts,
    userFavoritedPosts,
    userLikedComments
  };
}); 