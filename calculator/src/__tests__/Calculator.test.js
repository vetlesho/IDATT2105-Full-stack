import { flushPromises, mount } from '@vue/test-utils';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import Calculator from '@/components/Calculator.vue';
import axios from 'axios';
//import AlertPopup from '@/components/AlertPopup.vue';

vi.mock('axios');

describe('Calculator.vue', () => {
  let wrapper;

  beforeEach(() => {
    vi.clearAllMocks();
    wrapper = mount(Calculator);
  });

  it('renders correctly', () => {
    expect(wrapper.find('input').exists()).toBe(true);
    expect(wrapper.findAll('button').length).toBeGreaterThan(19);
  });

  it('appends values to the input correctly', async () => {
    // Find button with text content "7" and "8"
    const button7 = wrapper.findAll('button').find(btn => btn.text() === '7');
    const button8 = wrapper.findAll('button').find(btn => btn.text() === '8');

    await button7.trigger('click');
    await button8.trigger('click');

    expect(wrapper.vm.input).toBe('78');
  });

  it('clears the input correctly', async () => {
    const button7 = wrapper.findAll('button').find(btn => btn.text() === '7');
    const buttonClear = wrapper.findAll('button').find(btn => btn.text() === 'C');

    await button7.trigger('click');
    expect(wrapper.vm.input).toBe('7');

    await buttonClear.trigger('click');
    expect(wrapper.vm.input).toBe('');
  });

  it('calculates addition correctly', async () => {
    axios.post.mockResolvedValue({
      data: {
        result: 15
      }
    });

    const buttons = wrapper.findAll('button');
    await buttons.find(btn => btn.text() === '7').trigger('click');
    await buttons.find(btn => btn.text() === '+').trigger('click');
    await buttons.find(btn => btn.text() === '8').trigger('click');
    await buttons.find(btn => btn.text() === '=').trigger('click');
    await flushPromises();

    expect(wrapper.vm.input).toBe('15');
    expect(axios.post).toHaveBeenCalledWith(
      'http://localhost:8080/api/calculator/calculate',
      { expression: '7+8' }
    );
  });

  it('calculates subtraction correctly', async () => {
    axios.post.mockResolvedValue({
      data: { result: 5 }
    });

    const buttons = wrapper.findAll('button');
    await buttons.find(btn => btn.text() === '9').trigger('click');
    await buttons.find(btn => btn.text() === '-').trigger('click');
    await buttons.find(btn => btn.text() === '4').trigger('click');
    await buttons.find(btn => btn.text() === '=').trigger('click');
    await flushPromises();

    expect(wrapper.vm.input).toBe('5');
  });

  it('calculates multiplication correctly', async () => {
    axios.post.mockResolvedValue({
      data: { result: 18 }
    });

    const buttons = wrapper.findAll('button');
    await buttons.find(btn => btn.text() === '6').trigger('click');
    await buttons.find(btn => btn.text() === '*').trigger('click');
    await buttons.find(btn => btn.text() === '3').trigger('click');
    await buttons.find(btn => btn.text() === '=').trigger('click');
    await flushPromises();

    expect(wrapper.vm.input).toBe('18');
  });

  it('calculates division correctly', async () => {
    axios.post.mockResolvedValue({
      data: { result: 4 }
    });

    const buttons = wrapper.findAll('button');
    await buttons.find(btn => btn.text() === '8').trigger('click');
    await buttons.find(btn => btn.text() === '/').trigger('click');
    await buttons.find(btn => btn.text() === '2').trigger('click');
    await buttons.find(btn => btn.text() === '=').trigger('click');
    await flushPromises();

    expect(wrapper.vm.input).toBe('4');
  });

  it('shows an error when trying to divide by zero', async () => {
    const buttons = wrapper.findAll('button');
    const button7 = buttons.find(btn => btn.text() === '7');
    const buttonDivide = buttons.find(btn => btn.text() === '/');
    const button0 = buttons.find(btn => btn.text() === '0');
    const buttonEquals = buttons.find(btn => btn.text() === '=');

    await button7.trigger('click');
    await buttonDivide.trigger('click');
    await button0.trigger('click');
    await buttonEquals.trigger('click');

    await wrapper.vm.$nextTick();

    const alertMessage = wrapper.vm.alertMessage.message;
    expect(alertMessage).toBe('Cannot divide by zero');
  });

  it('does not allow an expression to end with an operator', async () => {
    const buttons = wrapper.findAll('button');
    const button7 = buttons.find(btn => btn.text() === '7');
    const buttonPlus = buttons.find(btn => btn.text() === '+');
    const buttonEquals = buttons.find(btn => btn.text() === '=');

    await button7.trigger('click');
    await buttonPlus.trigger('click');
    await buttonEquals.trigger('click');

    await wrapper.vm.$nextTick();

    const alertMessage = wrapper.vm.alertMessage.message;
    expect(alertMessage).toBe('Expression cannot end with an operator');
  });

  it('appends to the input without repeating consecutive operators', async () => {
    const buttons = wrapper.findAll('button');
    const button7 = buttons.find(btn => btn.text() === '7');
    const buttonPlus = buttons.find(btn => btn.text() === '+');

    await button7.trigger('click');
    await buttonPlus.trigger('click');
    await buttonPlus.trigger('click');

    expect(wrapper.vm.input).toBe('7+');
  });

  it('handles the negative value change correctly', async () => {
    const buttons = wrapper.findAll('button');
    const button5 = buttons.find(btn => btn.text() === '5');
    const buttonChangeValue = buttons.find(btn => btn.text() === '+/-');

    await button5.trigger('click');
    expect(wrapper.vm.input).toBe('5');

    // switch to negative
    await buttonChangeValue.trigger('click');
    expect(wrapper.vm.input).toBe('-5');

    // back to positive
    await buttonChangeValue.trigger('click');
    expect(wrapper.vm.input).toBe('5');
  });

  it('handles API errors correctly', async () => {
    axios.post.mockRejectedValue(new Error('Network Error'));

    const buttons = wrapper.findAll('button');
    await buttons.find(btn => btn.text() === '7').trigger('click');
    await buttons.find(btn => btn.text() === '+').trigger('click');
    await buttons.find(btn => btn.text() === '8').trigger('click');
    await buttons.find(btn => btn.text() === '=').trigger('click');

    await flushPromises();

    expect(wrapper.vm.alertMessage).toBe('Error: Network Error');
  });

  it('handles the calculation log correctly', async () => {
    // Mock first calculation response
    axios.post.mockResolvedValueOnce({
      data: { result: 15 }
    });
    // Mock second calculation response
    axios.post.mockResolvedValueOnce({
      data: { result: 15 }
    });

    const buttons = wrapper.findAll('button');
    await buttons.find(btn => btn.text() === '7').trigger('click');
    await buttons.find(btn => btn.text() === '+').trigger('click');
    await buttons.find(btn => btn.text() === '8').trigger('click');
    await buttons.find(btn => btn.text() === '=').trigger('click');

    await flushPromises(); // Wait for first API call

    expect(wrapper.vm.calculationLog.length).toBe(1);
    expect(wrapper.vm.calculationLog[0]).toBe('7+8 = 15');

    // Test second calculation
    await buttons.find(btn => btn.text() === '=').trigger('click');
    await flushPromises(); // Wait for second API call

    expect(wrapper.vm.calculationLog.length).toBe(2);
    expect(wrapper.vm.calculationLog[0]).toBe('15 = 15');

    // Verify API calls
    expect(axios.post).toHaveBeenCalledTimes(2);
    expect(axios.post).toHaveBeenNthCalledWith(1,
      'http://localhost:8080/api/calculator/calculate',
      { expression: '7+8' }
    );
    expect(axios.post).toHaveBeenNthCalledWith(2,
      'http://localhost:8080/api/calculator/calculate',
      { expression: '15' }
    );
  });
});
