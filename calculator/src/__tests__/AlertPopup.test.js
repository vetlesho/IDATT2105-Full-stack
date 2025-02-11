import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import AlertPopup from '@/components/AlertPopup.vue'

describe('AlertPopup.vue', () => {
  it('renders message when visible', async () => {
    const wrapper = mount(AlertPopup, {
      props: {
        message: 'Test message'
      }
    })

    // Initially hidden
    expect(wrapper.isVisible()).toBe(false)

    // Show the alert
    await wrapper.vm.show()
    expect(wrapper.isVisible()).toBe(true)
    expect(wrapper.text()).toContain('Test message')
  })

  it('emits event when closed', async () => {
    const wrapper = mount(AlertPopup, {
      props: {
        message: 'Test message'
      }
    })

    await wrapper.vm.show()
    await wrapper.find('button').trigger('click')

    // Check if alert is hidden
    expect(wrapper.isVisible()).toBe(false)
    // Check if event was emitted
    expect(wrapper.emitted('alert-closed')).toBeTruthy()
  })

  it('handles empty message prop', () => {
    const wrapper = mount(AlertPopup, {
      props: {
        message: ''
      }
    })

    expect(() => wrapper.vm.show()).not.toThrow()
  })
})
