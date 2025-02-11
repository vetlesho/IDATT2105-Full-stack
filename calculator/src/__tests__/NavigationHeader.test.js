import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import NavigationHeader from '@/components/NavigationHeader.vue'
import { createRouter, createWebHistory } from 'vue-router'

// Create a mock router
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: {} },
    { path: '/Calculator', component: {} },
    { path: '/Contactform', component: {} }
  ]
})

describe('NavigationHeader.vue', () => {
  it('renders the header title correctly', () => {
    const wrapper = mount(NavigationHeader, {
      global: {
        plugins: [router]
      }
    })
    expect(wrapper.find('h1').text()).toBe('Calculator')
  })

  it('shows correct navigation links', () => {
    const wrapper = mount(NavigationHeader, {
      global: {
        plugins: [router]
      }
    })
    const links = wrapper.findAll('.nav-link')
    expect(links).toHaveLength(2)
    expect(links[0].text()).toBe('Home')
  })

  it('toggles between Calculator and Contact Form link text', async () => {
    const wrapper = mount(NavigationHeader, {
      global: {
        plugins: [router]
      }
    })

    // Initial state (at root path)
    await router.push('/')
    expect(wrapper.findAll('.nav-link')[1].text()).toBe('Calculator')

    // Navigate to Calculator
    await router.push('/Calculator')
    expect(wrapper.findAll('.nav-link')[1].text()).toBe('Contact Form')

    // Navigate to Contact Form
    await router.push('/Contactform')
    expect(wrapper.findAll('.nav-link')[1].text()).toBe('Calculator')
  })
})
