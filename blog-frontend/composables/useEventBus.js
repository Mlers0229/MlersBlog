/**
 * 简单的事件总线，用于组件间通信
 * 可以通过事件总线来触发和监听事件，而不需要组件之间的直接引用
 */

const listeners = new Map();

export function useEventBus() {
  /**
   * 监听事件
   * @param {string} event - 事件名称
   * @param {Function} callback - 回调函数
   */
  const on = (event, callback) => {
    if (!listeners.has(event)) {
      listeners.set(event, []);
    }
    listeners.get(event).push(callback);
    
    // 返回一个取消监听的函数
    return () => off(event, callback);
  };
  
  /**
   * 移除事件监听
   * @param {string} event - 事件名称
   * @param {Function} callback - 回调函数
   */
  const off = (event, callback) => {
    if (!listeners.has(event)) return;
    
    const callbacks = listeners.get(event);
    const index = callbacks.indexOf(callback);
    
    if (index !== -1) {
      callbacks.splice(index, 1);
    }
    
    if (callbacks.length === 0) {
      listeners.delete(event);
    }
  };
  
  /**
   * 触发事件
   * @param {string} event - 事件名称
   * @param {*} payload - 事件数据
   */
  const emit = (event, payload) => {
    if (!listeners.has(event)) return;
    
    listeners.get(event).forEach(callback => {
      // 使用setTimeout确保回调在当前执行栈之外执行
      // 这样可以避免可能的死锁和错误传播
      setTimeout(() => callback(payload), 0);
    });
  };
  
  /**
   * 移除所有特定事件的监听器
   * @param {string} event - 事件名称
   */
  const reset = (event) => {
    if (event) {
      listeners.delete(event);
    } else {
      listeners.clear();
    }
  };
  
  return {
    on,
    off,
    emit,
    reset
  };
} 