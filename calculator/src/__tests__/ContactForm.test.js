import { mount } from '@vue/test-utils';
import { describe, it, expect, vi } from 'vitest';
import ContactForm from '@/components/ContactForm.vue';
//import AlertPopup from '@/components/AlertPopup.vue';
import { createTestingPinia } from '@pinia/testing';
import axios from 'axios';

// Mock Axios
vi.mock('axios');

describe('ContactForm.vue', () => {
  it("Loads the page correctly", () => {
    const wrapper = mount(ContactForm, {
      global: {
        plugins: [createTestingPinia()], // Mock Pinia store
      },
    });

    expect(wrapper.find('h1').text()).toBe('Give feedback!');
    expect(wrapper.find('input[placeholder="Full Name"]').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="E-mail"]').exists()).toBe(true);
    expect(wrapper.find('textarea[placeholder="Feedback"]').exists()).toBe(true);
    expect(wrapper.find('button').text()).toBe('Submit');
  });

  it("Displays correct erros message when input is wrong", async () => {
    const wrapper = mount(ContactForm, {
      global: {
        plugins: [createTestingPinia()]
      }
    });

    // Name input
    const nameInput = wrapper.find('input[placeholder="Full Name"]');
    await nameInput.setValue('123456789'); // Set invalid input (numbers)
    await nameInput.trigger('blur');

    const errorMessageNumbers = wrapper.find('input[placeholder="Full Name"] + span');
    expect(errorMessageNumbers.text()).toBe('Must be only letters');

    await nameInput.setValue('teste'); // Set invalid input for too short name
    await nameInput.trigger('blur');

    const errorMessageTooShort = wrapper.find('input[placeholder="Full Name"] + span');
    expect(errorMessageTooShort.text()).toBe('This field should be at least 6 characters long');

    // Email input
    await wrapper.find('input[placeholder="E-mail"]').setValue('invalidemail');
    await wrapper.find('input[placeholder="E-mail"]').trigger('blur');

    const errorMessageEmail = wrapper.find('input[placeholder="E-mail"] + span');
    expect(errorMessageEmail.text()).toBe("Value is not a valid email address");

    // Feedback input
    await wrapper.find('textarea[placeholder="Feedback"]').setValue('Short');
    await wrapper.find('textarea[placeholder="Feedback"]').trigger('blur');

    const errorMessageTextArea = wrapper.find('textarea[placeholder="Feedback"] + span');
    expect(errorMessageTextArea.text()).toBe("This field should be at least 10 characters long");
  });

  it('disables the submit button when form is invalid', async () => {
    const wrapper = mount(ContactForm, {
      global: {
        plugins: [createTestingPinia()],
      },
    });

    await wrapper.find('input[placeholder="Full Name"]').setValue('123456789');
    await wrapper.find('input[placeholder="E-mail"]').setValue('invalidemail');
    await wrapper.find('textarea[placeholder="Feedback"]').setValue('Short');

    const button = wrapper.find('button');
    expect(button.attributes('disabled')).toBeDefined();
  });

  it('submits form data correctly', async () => {
    // Mock successful POST response
    axios.post.mockResolvedValue({ status: 201 });

    const wrapper = mount(ContactForm, {
      global: {
        plugins: [createTestingPinia()],
      }
    });

    await wrapper.find('input[placeholder="Full Name"]').setValue('Vetle Test');
    await wrapper.find('input[placeholder="E-mail"]').setValue('vetlehodne@gmail.com');
    await wrapper.find('textarea[placeholder="Feedback"]').setValue('This is a feedback message.');
    await wrapper.find('button').trigger('click');

    // Verify POST request was made with correct data
    expect(axios.post).toHaveBeenCalledWith('http://localhost:3000/feedback', {
      name: 'Vetle Test',
      email: 'vetlehodne@gmail.com',
      feedback: 'This is a feedback message.',
    });
  });
});
