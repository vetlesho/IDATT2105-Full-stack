import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import AlertPopup from '@/components/AlertPopup.vue'

describe('AlertPopup.vue', () => {
  it('shows alert with message', async () => {
    const wrapper = mount(AlertPopup)
    const testMessage = 'Test message'

    // Initially hidden
    expect(wrapper.vm.isVisible).toBe(false)

    // Show alert with message
    await wrapper.vm.showAlert(testMessage)

    expect(wrapper.vm.isVisible).toBe(true)
    expect(wrapper.vm.message).toBe(testMessage)
    expect(wrapper.text()).toContain(testMessage)
  })

  it('closes alert when OK button is clicked', async () => {
    const wrapper = mount(AlertPopup)

    // Show the alert first
    await wrapper.vm.showAlert('Test message')
    expect(wrapper.vm.isVisible).toBe(true)

    // Click the close button
    await wrapper.find('button').trigger('click')

    expect(wrapper.vm.isVisible).toBe(false)
    expect(wrapper.emitted('alert-closed')).toBeTruthy()
  })

  it('updates message when showing new alert', async () => {
    const wrapper = mount(AlertPopup)

    await wrapper.vm.showAlert('First message')
    expect(wrapper.vm.message).toBe('First message')

    await wrapper.vm.showAlert('Second message')
    expect(wrapper.vm.message).toBe('Second message')
  })

  it('handles empty message', async () => {
    const wrapper = mount(AlertPopup)

    await wrapper.vm.showAlert('')
    expect(wrapper.vm.isVisible).toBe(true)
    expect(wrapper.vm.message).toBe('')
  })
})
