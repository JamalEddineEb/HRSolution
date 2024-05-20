export interface IResume {
  id: number;
  name?: string | null;
  document?: string | null;
  documentContentType?: string | null;
}

export type NewResume = Omit<IResume, 'id'> & { id: null };
